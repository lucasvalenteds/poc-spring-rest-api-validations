package com.example.spring.account;

import com.example.spring.validations.AccountHasEnoughBalance;
import com.example.spring.validations.AccountIsNotLocked;
import com.example.spring.validation.ValidationErrors;
import com.example.spring.validation.Validations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
public final class AccountController {

    private final AccountRepository repository;

    public AccountController(AccountRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Mono<ResponseEntity<Account>> create() {
        return Mono.fromCallable(() -> repository.save(new Account(BigDecimal.ZERO)))
            .map(account -> ResponseEntity.ok().body(account));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Account>> findById(@PathVariable("id") String uuid) {
        return Mono.fromCallable(() -> UUID.fromString(uuid))
            .flatMap(id -> Mono.fromCallable(() ->
                repository.findById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build())
            ));
    }

    @PostMapping("/{id}/deposit")
    public Mono<ResponseEntity<AccountBalance>> deposit(@PathVariable("id") String uuid,
                                                        @RequestBody AccountBalanceAmount amount) {
        return Mono.fromCallable(() -> repository.findById(UUID.fromString(uuid)))
            .flatMap(optional -> optional.map(Mono::just).orElseGet(Mono::empty))
            .map(account -> new Account(account.getId(), account.getBalance().add(amount.getAmount())))
            .flatMap(account -> Mono.fromCallable(() -> repository.save(account)))
            .map(account -> new AccountBalance(account.getBalance()))
            .map(ResponseEntity::ok)
            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @PostMapping("/{id}/draw")
    public Mono<ResponseEntity<AccountBalance>> draw(@PathVariable("id") String uuid,
                                                     @RequestBody AccountBalanceAmount amount) {
        return Mono.fromCallable(() -> repository.findById(UUID.fromString(uuid)))
            .flatMap(optional -> optional.map(Mono::just).orElseGet(Mono::empty))
            .map(account -> new Account(account.getId(), account.getBalance().subtract(amount.getAmount())))
            .flatMap(account -> Mono.fromCallable(() -> repository.save(account)))
            .map(account -> new AccountBalance(account.getBalance()))
            .map(ResponseEntity::ok)
            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @GetMapping("/{id}/draw/validations")
    public Mono<ResponseEntity<ValidationErrors>> validateDrawOperation(@PathVariable("id") String uuid) {
        Mono<Account> accountToDrawFrom = Mono.fromCallable(() -> repository.findById(UUID.fromString(uuid)))
            .flatMap(it -> it.map(Mono::just).orElseGet(Mono::empty));

        return Mono.from(accountToDrawFrom)
            .flatMap(account -> Validations.validate(List.of(
                new AccountIsNotLocked(account),
                new AccountHasEnoughBalance(account)
            )))
            .map(ResponseEntity::ok)
            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
}
