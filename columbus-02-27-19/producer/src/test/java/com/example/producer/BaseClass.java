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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TicketWebConfig.class)
@ExtendWith(SpringExtension.class)
public class BaseClass {
    @LocalServerPort
    int port;

    @MockBean
    TicketRepository repo;

    @BeforeEach
    void setup() {
        Mockito.when(repo.findAll()).thenReturn(Flux.just(new Ticket("1234", "Train")));

        RestAssured.baseURI = "http://localhost:" + port;
    }
}
