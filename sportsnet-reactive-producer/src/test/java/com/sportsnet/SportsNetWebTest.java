package com.sportsnet;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@WebFluxTest
@RunWith(MockitoJUnitRunner.class)
public class SportsNetWebTest {

    private SportsNetWebConfig webConfig = new SportsNetWebConfig();

    @Mock
    private TeamRepository repository;

    private Team red = new Team("1", "REDZS");
    private Team blue = new Team("2", "BLUES");

    @Before
    public void before() {
        Mockito
                .when(this.repository.findAll())
                .thenReturn(Flux.just(red, blue));

        Mockito
                .when(this.repository.getMyFavorites())
                .thenReturn(Flux.just(blue));

        Mockito
                .when(this.repository.findByName(Mockito.anyString()))
                .thenReturn(Mono.just(blue));

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
    public void testShouldBetByName() throws JsonProcessingException {

        String jsonBlob = "{name:'BLUES', id: '2'}";

        WebTestClient
                .bindToRouterFunction(webConfig.routes(repository))
                .build()
                .get().uri("/teams/byName?name=BLUES")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .json(jsonBlob); // is this semantic? YES
    }

    @Test
    public void testShouldGetFavs() {

        Flux<Team> webResponseTeams = WebTestClient
                .bindToRouterFunction(webConfig.routes(repository))
                .build()
                .get().uri("/teams/favorites")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .returnResult(Team.class)
                .getResponseBody();

        StepVerifier
                .create(webResponseTeams)
                .expectSubscription()
                .expectNext(new Team("2", "BLUES"))
                .expectNextCount(0)     // because why not?
                .verifyComplete();

    }
}