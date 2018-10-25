package com.sportsnet;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"server.port=0"}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseClass {

    @LocalServerPort
    private int port;

    @Configuration
    @Import(SportsNetWebConfig.class)
    public static class TestConfiguration {
    }

    @MockBean
    private TeamRepository repository;

    @Before
    public void before() {

        RestAssured.baseURI = "http://localhost:" + this.port;

        Mockito
                .when(this.repository.findAll())
                .thenReturn(Flux.just(new Team("1", "REDS"), new Team("2", "BLUES")));

        Mockito
                .when(this.repository.getMyFavorites())
                .thenReturn(Flux.just(new Team("2", "BLUES")));

    }
}