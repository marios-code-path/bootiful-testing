package com.example.producer;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(MessageRouters.class)
public class BaseClass {

    @LocalServerPort
    private int port;

    @MockBean
    private ReactiveMongoRepository<Message, UUID> repo;

    @BeforeEach
    void setUp() {
        Mockito
                .when(repo.findAll())
                .thenReturn(Flux.just(new Message(UUID.fromString("7e43b2fc-59db-11e9-8647-d663bd873d93"), "HELLO")));
        RestAssured.baseURI = "http://localhost:" + port;
    }
}
