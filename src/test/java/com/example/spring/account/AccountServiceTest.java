package com.example.spring.account;

import com.example.spring.Main;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = Main.class)
class AccountServiceTest {

    private AccountService service;

    @BeforeEach
    void beforeEach(ApplicationContext context) {
        AccountRepository repository = context.getBean(AccountRepository.class);
        service = new AccountService(repository);
    }

    @Test
    void testFailingToCreateUUIDFromInvalidUUID() {
        StepVerifier.create(service.createUUID("not-uuid-here"))
            .expectError(Exception.class)
            .verify();
    }

    @Test
    void testFailingToSaveWhenAccountIsNull() {
        StepVerifier.create(service.save(null))
            .expectError(Exception.class)
            .verify();
    }

    @Test
    void testFailingToFindByIdWhenIdIsNull() {
        StepVerifier.create(service.findById(null))
            .expectError(Exception.class)
            .verify();
    }
}