package com.example.producer;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.UUID;

@WebFluxTest
@ExtendWith(SpringExtension.class)
public class UserRestTests {

    @MockBean
    private UserRepository repo;

    private DemoUser demoUser = new DemoUser(UUID.randomUUID(), "Chris");

    @BeforeEach
    public void setUp() {
        Mockito.when(repo.findAll())
                .thenReturn(Flux.just(demoUser));
    }

    @Test
    public void testShouldGetAll() {
        WebTestClient
                .bindToRouterFunction(new DemoRestRoutes(repo).demoUserRoutes())
                .build()
                .get()
                .uri("/all")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.[0].id").isNotEmpty()
                .jsonPath("$.[0].name").isEqualTo("Chris");

    }
}
