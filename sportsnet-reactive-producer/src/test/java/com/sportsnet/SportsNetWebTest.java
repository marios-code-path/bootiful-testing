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
import reactor.core.publisher.Hooks;
import reactor.test.StepVerifier;

@WebFluxTest
@Import(SportsNetWebConfig.class)
@RunWith(SpringRunner.class)
public class SportsNetWebTest {


    @Autowired
    private SportsNetWebConfig webConfig;

    @MockBean
    private TeamRepository repository;

    Team red = new Team("1", "REDZS");
    Team blue = new Team("2", "BLUES");

    @Before
    public void before() {

        Mockito
                .when(this.repository.findAll())
                .thenReturn(Flux.just(red, blue));

        Mockito
                .when(this.repository.getMyFavorites())
                .thenReturn(Flux.just(blue));

        Hooks.onOperatorDebug();
    }

    @Test
    public void testShouldGetAll() {

        WebTestClient
                .bindToRouterFunction(webConfig.routes(repository))
                .build()
                .get().uri("/teams/all")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.[0].id").isEqualTo("1")
                .jsonPath("$.[0].name").isEqualTo("REDZS")
                .jsonPath("$.[1].id").isEqualTo("2")
                .jsonPath("$.[1].name").isEqualTo("BLUES");
    }

    @Test
    public void testShouldGetFavs() {

        StepVerifier.create(WebTestClient
                .bindToRouterFunction(webConfig.routes(repository))
                .build()
                .get().uri("/teams/favorite")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .returnResult(Team.class)
                .getResponseBody()
        )
                .expectSubscription()
                .expectNext(new Team("2", "BLUES"))
                .verifyComplete();

    }
}