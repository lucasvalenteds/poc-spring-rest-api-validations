package com.example.spring.account;

import com.example.spring.validations.AccountHasEnoughBalance;
import com.example.spring.validations.AccountIsNotLocked;
import com.example.spring.validation.ValidationErrors;
import com.example.spring.validation.Validations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(path = "/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
public final class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @PostMapping
    public Mono<ResponseEntity<Account>> create() {
        return service.save(new Account(BigDecimal.ZERO, true))
            .map(account -> ResponseEntity.ok().body(account));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Account>> findById(@PathVariable("id") String uuid) {
        return service.createUUID(uuid)
            .flatMap(service::findById)
            .map(ResponseEntity::ok)
            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @PatchMapping("/{id}/lock")
    public Mono<ResponseEntity<Object>> lock(@PathVariable("id") String uuid) {
        return service.createUUID(uuid)
            .flatMap(service::findById)
            .map(account -> {
                account.setLocked(true);
                return account;
            })
            .flatMap(service::save)
            .map(account -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).build())
            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @PatchMapping("/{id}/unlock")
    public Mono<ResponseEntity<Object>> unlock(@PathVariable("id") String uuid) {
        return service.createUUID(uuid)
            .flatMap(service::findById)
            .map(account -> {
                account.setLocked(false);
                return account;
            })
            .flatMap(service::save)
            .map(account -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).build())
            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @PostMapping("/{id}/deposit")
    public Mono<ResponseEntity<AccountBalance>> deposit(@PathVariable("id") String uuid,
                                                        @RequestBody AccountBalanceAmount amount) {
        return service.createUUID(uuid)
            .flatMap(service::findById)
            .map(account -> new Account(
                account.getId(),
                account.getBalance().add(amount.getAmount()),
                account.getLocked()
            ))
            .flatMap(service::save)
            .map(account -> new AccountBalance(account.getBalance()))
            .map(ResponseEntity::ok)
            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @PostMapping("/{id}/draw")
    public Mono<ResponseEntity<AccountBalance>> draw(@PathVariable("id") String uuid,
                                                     @RequestBody AccountBalanceAmount amount) {
        return service.createUUID(uuid)
            .flatMap(service::findById)
            .map(account -> new Account(
                account.getId(),
                account.getBalance().subtract(amount.getAmount()),
                account.getLocked()
            ))
            .flatMap(service::save)
            .map(account -> new AccountBalance(account.getBalance()))
            .map(ResponseEntity::ok)
            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @GetMapping("/{id}/draw/validations")
    public Mono<ResponseEntity<ValidationErrors>> validateDrawOperation(@PathVariable("id") String uuid) {
        return service.createUUID(uuid)
            .flatMap(service::findById)
            .flatMap(account -> Validations.validate(List.of(
                new AccountIsNotLocked(account),
                new AccountHasEnoughBalance(account)
            )))
            .map(ResponseEntity::ok)
            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
}
