package com.sportsnet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

@WebFluxTest
@Import(SportsNetWebConfig.class)
@RunWith(SpringRunner.class)
public class SportsNetWebTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private TeamRepository repository;

    @Before
    public void before() {

        Mockito
                .when(this.repository.findAll())
                .thenReturn(Flux.just(new Team("1", "REDS"), new Team("2", "BLUES")));
    }

    @Test
    public void getAll() {

        this.client
                .get()
                .uri("/teams/all")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.[0].id").isEqualTo("1")
                .jsonPath("$.[0].name").isEqualTo("REDS")
                .jsonPath("$.[1].id").isEqualTo("2")
                .jsonPath("$.[1].name").isEqualTo("BLUES");
    }
}