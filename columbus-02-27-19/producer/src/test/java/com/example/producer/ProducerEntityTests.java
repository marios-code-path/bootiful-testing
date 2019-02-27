package com.example.producer;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@DataMongoTest

public class ProducerEntityTests {

    @Autowired
    TicketRepository repo;

    Matcher<Ticket> ticketMatcher = Matchers.allOf(
            Matchers.notNullValue(),
            Matchers.hasProperty("id", Matchers.notNullValue()),
            Matchers.hasProperty("name", Matchers.equalTo("Traine"))
    );

    @org.junit.jupiter.api.Test
    public void testEntityShouldSaveFind() {
        Ticket t = new Ticket("1234", "Train");

        Publisher<Ticket> save = repo.save(t);
        Publisher<Ticket> finder = repo.findAll();

        Publisher<Ticket> composite = Flux.from(save)
                .thenMany(finder);

        StepVerifier
                .create(composite)
                .expectSubscription()
                .assertNext(it -> MatcherAssert.assertThat("Has Proper State", it, ticketMatcher))
                .expectComplete()
                .verify();
    }


}
