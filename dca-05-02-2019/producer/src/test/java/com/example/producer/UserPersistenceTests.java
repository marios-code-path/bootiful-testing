package com.example.producer;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.UUID;


@DataMongoTest
@ExtendWith(SpringExtension.class)
public class UserPersistenceTests {

    @Autowired
    private UserRepository repo;

    @Test
    public void testShouldSaveFind() {
        DemoUser user = new DemoUser(UUID.randomUUID(), "PureLife");

        Flux<DemoUser> userStream = repo
                .save(user)
                .thenMany(repo.findAll());

        StepVerifier
                .create(userStream)
                .assertNext(it ->
                        Assertions
                                .assertThat(it)
                                .as("a demo user has state")
                                .isNotNull()
                                .hasNoNullFieldsOrProperties()
                )
                .verifyComplete();
    }

    @Test
    public void testShouldStream() {
        DemoUser user = new DemoUser(UUID.randomUUID(), "PureLife");

        Publisher userFlux = Flux
                .just(user)
                .repeat(1);

        StepVerifier
                .create(userFlux)
                .expectSubscription()
                .assertNext(it ->
                        Assertions
                                .assertThat(it)
                                .as("a demo user has state")
                                .isNotNull()
                                .hasNoNullFieldsOrProperties()
                )
                .assertNext(it ->
                        Assertions
                                .assertThat(it)
                                .as("a demo user has state")
                                .isNotNull()
                                .hasNoNullFieldsOrProperties()
                )
                .expectComplete()
                .verify();

    }

}
