package com.example.producer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

@ExtendWith(SpringExtension.class)
@WebFluxTest
public class DemoWebFluxTest {

    @MockBean
    UserRepository repo;

    @Autowired
    WebTestClient testClientAuto;

    @BeforeEach
    public void setUp() {
        Mockito.when(repo.findAll())
                .thenReturn(Flux.just(new User("1234", "John")));
    }

    @Test
    public void testWebFindAll() {
        WebTestClient
                .bindToRouterFunction(new UserRoutes().route(repo))
                .build()
                .get()
                .uri("/all")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.[0].id").isEqualTo("1234")
                .jsonPath("$.[0].name").isEqualTo("John");
    }
}
