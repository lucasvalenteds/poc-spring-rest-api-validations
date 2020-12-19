package com.example.spring.account;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
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
}
