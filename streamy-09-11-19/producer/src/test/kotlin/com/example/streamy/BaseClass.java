package com.example.streamy;

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
    private MessageService service;

    @BeforeEach
    void setUp() {
        Mockito
                .when(service.get(Mockito.anyString()))
                .thenReturn(
                        Flux.just(new Message(new UUID(123456L, 0L), "Mario", "Demo Time"))
                );
        RestAssured.baseURI = "http://localhost:" + port + "/";
    }
}
