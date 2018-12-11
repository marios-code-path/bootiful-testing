package com.example.demoproducer;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class BaseClass {

    @LocalServerPort
    private Long port;

    @MockBean
    UserRepository repo;

    private User user1 = new User("1234", "Mario");
    private User user2 = new User("2345", "Dave");

    @BeforeAll
    public void setUp() {
        Mockito.when(repo.findAll())
                .thenReturn(Flux.just(user1, user2));

        RestAssured.baseURI = "http://localhost:" + port;
    }
}
