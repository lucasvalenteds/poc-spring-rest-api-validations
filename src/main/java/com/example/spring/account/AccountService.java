package com.example.spring.account;

import reactor.core.publisher.Mono;

import java.util.UUID;

public final class AccountService {

    private final AccountRepository repository;

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    public Mono<UUID> createUUID(String uuid) {
        return Mono.create(sink -> {
            try {
                sink.success(UUID.fromString(uuid));
            } catch (IllegalArgumentException exception) {
                sink.error(exception);
            }
        });
    }

    public Mono<Account> save(Account account) {
        return Mono.create(sink -> {
            try {
                sink.success(repository.save(account));
            } catch (Exception exception) {
                sink.error(exception);
            }
        });
    }

    public Mono<Account> findById(UUID id) {
        return Mono.create(sink -> {
            try {
                repository.findById(id).ifPresentOrElse(
                    sink::success,
                    sink::success
                );
            } catch (Exception exception) {
                sink.error(exception);
            }
        });
    }
}
