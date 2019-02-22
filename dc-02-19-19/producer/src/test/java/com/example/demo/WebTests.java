package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

@WebFluxTest
@Import(WebConfig.class)
public class WebTests {

    @Autowired
    WebTestClient testClient;

    @MockBean
    PersonRepository personRepo;


    @BeforeEach
    void setup() {
        Mockito.when(personRepo.findAll())
                .thenReturn(Flux.just(new Person("1234", "Bob")));
    }

    @Test
    void testShouldGetAll() {
        WebTestClient.bindToRouterFunction(new WebConfig().route(personRepo))
                .build().get()
                .uri("/all")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.[0].name").isEqualTo("Bob");
    }
}
