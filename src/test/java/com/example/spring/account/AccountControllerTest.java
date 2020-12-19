package com.example.spring.account;

import com.example.spring.Main;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = Main.class)
class AccountControllerTest {

    private AccountRepository repository;

    private WebTestClient client;

    private Account account;

    @BeforeEach
    void beforeEach(ApplicationContext context) {
        repository = context.getBean(AccountRepository.class);
        client = WebTestClient.bindToApplicationContext(context).build();

        repository.deleteAll();

        account = repository.save(new Account(BigDecimal.valueOf(125.49)));
        assertNotNull(account);
    }

    @Test
    void testCreatingNewAccount() {
        client.post()
            .uri("/accounts")
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id").value(id -> assertNotNull(UUID.fromString(id.toString())))
            .jsonPath("$.balance").isEqualTo(0L);
    }

    @Test
    void testFindingAccountById() {
        client.get()
            .uri("/accounts/{accountId}", Map.of("accountId", account.getId().toString()))
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id").isEqualTo(account.getId().toString())
            .jsonPath("$.balance").isEqualTo(account.getBalance());
    }

    @Test
    void testDepositingMoney() {
        BigDecimal amount = BigDecimal.valueOf(15.37);

        client.post()
            .uri("/accounts/{accountId}/deposit", Map.of("accountId", account.getId().toString()))
            .body(BodyInserters.fromValue(new AccountBalanceAmount(amount)))
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.balance").isEqualTo(account.getBalance().add(amount));
    }

    @Test
    void testDrawingMoney() {
        BigDecimal amount = BigDecimal.valueOf(15.37);

        client.post()
            .uri("/accounts/{accountId}/draw", Map.of("accountId", account.getId().toString()))
            .body(BodyInserters.fromValue(new AccountBalanceAmount(amount)))
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.balance").isEqualTo(account.getBalance().subtract(amount));
    }
}