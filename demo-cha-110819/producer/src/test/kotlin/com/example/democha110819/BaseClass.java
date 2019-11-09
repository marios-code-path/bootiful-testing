package com.example.democha110819;


import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import reactor.core.publisher.Flux;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseClass {

    @LocalServerPort
    private int port;

    @MockBean
    private TeamRepository repo;

    @BeforeEach
    void setUp() {
        Mockito
                .when(repo.findAll())
                .thenReturn(Flux.just(new Team(UUID.fromString("7e43b2fc-59db-11e9-8647-d663bd873d93"), "Panthers")));

        RestAssured.baseURI = "http://localhost:" + port + "/";
    }
}