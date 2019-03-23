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
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@Import(CarRestService.class)
public class BaseClass {

    @LocalServerPort
    private Long port;

    @MockBean
    CarService service;

    private String key = "12345-67890-07061-97981";

    @BeforeEach
    public void setUp() {
        Mockito.when(service.newCar(any()))
                .thenReturn(Mono.just(key));
        Mockito.when(service.getCar(Mockito.anyString()))
                .thenReturn(Mono.just(new Car("Tesla", "Z", "Red")));

        RestAssured.baseURI = "http://localhost:" + port;
    }
}
