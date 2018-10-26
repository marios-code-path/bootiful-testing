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

    private Team red = new Team("1883", "Dodgers");
    private Team blue = new Team("1912", "RedSox");

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
                .jsonPath("$.[0].id").isEqualTo("1883")
                .jsonPath("$.[0].name").isEqualTo("Dodgers")
                .jsonPath("$.[1].id").isEqualTo("1912")
                .jsonPath("$.[1].name").isEqualTo("RedSox");
    }

    @Test
    public void testShouldBetByName() throws JsonProcessingException {

        String jsonBlob = "{name:'RedSox', id: '1912'}";

        WebTestClient
                .bindToRouterFunction(webConfig.routes(repository))
                .build()
                .get().uri("/teams/byName?name=RedSox")
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
                .expectNext(new Team("1912", "RedSox"))
                .expectNextCount(0)     // because why not?
                .verifyComplete();

    }
}