package com.demo.barcelona.producer;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(SpaceRouters.class)
@ExtendWith(SpringExtension.class)
public class BaseClass {

    @LocalServerPort
    private int port;

    @MockBean
    private StarRepository repo;

    UUID firstUUID = UUID.fromString("7e43b2fc-59db-11e9-8647-d663bd873d93");
    UUID secondUUID = UUID.fromString("7e43b2fd-59db-11e9-8647-d663bd873d93");

    @BeforeAll
    void setUp() {
        Mockito
                .when(repo.findAll())
                .thenReturn(
                        Flux.just(
                                new Star(firstUUID, "Vega", 0.03),
                                new Star(secondUUID, "Castor", 1.962)
                        )
                );
        RestAssured.baseURI = "http://localhost:" + port;
    }

}
