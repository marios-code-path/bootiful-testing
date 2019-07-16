package com.example.phi.demo;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
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
    private NoteRepository repo;

    @BeforeEach
    void setUp() {
        BDDMockito
                .given(repo.findAll())
                .willReturn(Flux.just(new Note(UUID.fromString("7e43b2fc-59db-11e9-8647-d663bd873d93"), "My Note")));
        RestAssured.baseURI = "http://localhost:" + port;
    }
}
