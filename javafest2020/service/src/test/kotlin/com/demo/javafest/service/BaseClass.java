package com.demo.javafest.service;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.BDDMockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseClass {

    @LocalServerPort
    private int port;

    @MockBean
    private ReactiveMongoRepository<Message, Long> repo;

    @BeforeEach
    public void  setUp() {
        BDDMockito
                .given(repo.findAll())
                .willReturn(
                        Flux.just(
                                new Message(1L, "Mario", "Luigi", "Pasta"),
                                new Message(2L, "Luigi", "Mario", "Meatball")
                        )
                );
        RestAssured.baseURI = "http://localhost:" + port;
    }

}