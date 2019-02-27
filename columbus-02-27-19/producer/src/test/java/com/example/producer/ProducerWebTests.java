package com.example.producer;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@WebFluxTest
@ExtendWith(SpringExtension.class)
public class ProducerWebTests {

    @Autowired
    WebTestClient   testClient;

    @MockBean
    TicketRepository repo;

    @BeforeEach
    public void setUp() {
        Mockito.when(repo.findAll())
                .thenReturn(Flux.just(
                        new Ticket("1234", "Train")
                ));
    }

    Matcher<Ticket> ticketMatcher = Matchers.allOf(
            Matchers.notNullValue(),
            Matchers.hasProperty("id", Matchers.notNullValue()),
            Matchers.hasProperty("name", Matchers.equalTo("Train"))
    );

    @Test
    public void testShouldFindAll() {
        WebTestClient
                .bindToRouterFunction(new TicketWebConfig().routes(repo))
                .build()
                .get()
                .uri("/all")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.[0].id").isEqualTo("1234")
                .jsonPath("$.[0].name").isEqualTo("Train");
    }

    @Test
    public void testShouldFindAllSecondly() {
        Flux<Ticket> tickets = WebTestClient
                .bindToRouterFunction(new TicketWebConfig().routes(repo))
                .build()
                .get()
                .uri("/all")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .returnResult(Ticket.class)
                .getResponseBody();

        StepVerifier
                .create(tickets)
                .expectSubscription()
                .assertNext(it-> MatcherAssert.assertThat("Response with valid state", it, ticketMatcher))
                .verifyComplete();
    }
}
