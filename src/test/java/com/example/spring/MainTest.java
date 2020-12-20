package com.example.spring;

import com.example.spring.account.Account;
import com.example.spring.validation.ValidationError;
import com.example.spring.validation.ValidationErrors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = Main.class)
class MainTest {

    private DisposableServer server;

    private WebClient client;

    @BeforeEach
    void beforeEach(ApplicationContext context) {
        server = context.getBean(HttpServer.class).bindNow();

        client = WebClient.builder()
            .baseUrl(String.format("http://localhost:%d", server.port()))
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }

    @AfterEach
    void afterEach() {
        server.disposeNow();
    }

    @Test
    void testDrawingFromNewlyCreatedAccountErrors() {
        Optional<Account> account = client.post()
            .uri("/accounts")
            .exchangeToMono(response -> response.bodyToMono(Account.class))
            .blockOptional();
        assertTrue(account.isPresent());

        client.patch()
            .uri("/accounts/{accountId}/unlock", Map.ofEntries(
                Map.entry("accountId", account.get().getId().toString()))
            )
            .exchangeToMono(response -> Mono.empty())
            .blockOptional();

        Optional<ValidationErrors> validationErrorsOptional = client.get()
            .uri("/accounts/{accountId}/draw/validations", Map.ofEntries(
                Map.entry("accountId", account.get().getId().toString())
            ))
            .exchangeToMono(response -> response.bodyToMono(ValidationErrors.class))
            .blockOptional();
        assertTrue(validationErrorsOptional.isPresent());

        ValidationErrors validationErrors = validationErrorsOptional.get();
        assertEquals(1, validationErrors.getErrors().size());

        ValidationError error = validationErrors.getErrors().get(0);
        assertEquals("balance", error.getField());
        assertEquals("Account.Operation.Draw.hasEnoughBalance", error.getMessage());
    }
}
