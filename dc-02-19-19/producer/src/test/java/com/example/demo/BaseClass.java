package com.example.demo;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Flux;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(WebConfig.class)
public class BaseClass {
    @LocalServerPort
    int port;

    @MockBean
    PersonRepository    personRepo;

    @BeforeEach
    void setup() {
        Mockito.when(personRepo.findAll())
                .thenReturn(Flux.just(new Person("1234", "Bob")));

        RestAssured.baseURI = "localhost:" + port;
    }
}
