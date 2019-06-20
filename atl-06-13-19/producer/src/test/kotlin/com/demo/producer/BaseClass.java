package com.demo.producer;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;

import java.util.UUID;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseClass {

    @LocalServerPort
    private int port;

    @MockBean
    private PeachRepository repo;

    @BeforeEach
    void setup() {
        Mockito
                .when(repo.findAll())
                .thenReturn(Flux.just(
                        new Peach(UUID.fromString("7e43b2fc-59db-11e9-8647-d663bd873d93"), "LARGE"),
                        new Peach(UUID.fromString("7e43b2fd-59db-11e9-8647-d663bd873d93"), "COBBLER")
                ));

        RestAssured.baseURI = "http://localhost:" + port;
    }

}
