package com.example.producer;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
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
@Import(DemoRestRoutes.class)
@ExtendWith(SpringExtension.class)
public class BaseClass {
    @LocalServerPort
    int port;

    @MockBean
    UserRepository repo;

    private UUID uuid = UUID.fromString("12345-67890-07061-97981");

    @BeforeEach
    void setup() {
        Mockito.when(repo.findAll()).thenReturn(Flux.just(new DemoUser(uuid, "John")));

        RestAssured.baseURI = "http://localhost:" + port;
    }
}