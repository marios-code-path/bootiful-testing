package com.demo.tanzu.service;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@Import(OrderRest.class)
public class BaseClass {

    @LocalServerPort
    private Long port;

    @MockBean
    private OrderRepositoryMongoDB repo;

    private String myId = "7e43b2fc-59db-11e9-8647-d663bd873d93";

    @BeforeEach
    public void setUp() {
        Mockito.when(repo.findAll())
                .thenReturn(
                        Flux.just(new Order(UUID.fromString(myId), "Valve", 3))
                );

        RestAssured.baseURI = "http://localhost:" + port;
    }
}
