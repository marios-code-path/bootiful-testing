package com.sportsnet;

import org.assertj.core.api.Assertions;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Date;
import java.util.function.Supplier;
import java.util.stream.Stream;


public class TeamTest {

    @Test
    public void testShouldCreate() {

        Team myTeam = new Team("1903", "Beşiktaş");

        Assert.assertNotNull(myTeam);
        Assert.assertEquals("1903", myTeam.getId());

        Assert.assertThat(myTeam, Matchers.notNullValue());

        Matcher compoundMatcher = Matchers.allOf(
                Matchers.notNullValue(),
                Matchers.equalTo("1903")
        );

        Assert.assertThat(myTeam.getId(), compoundMatcher);

        Assertions.assertThat(myTeam)
                .as("All properties were fulfilled")
                .hasNoNullFieldsOrProperties();

        Assertions.assertThat(myTeam.getId())
                .isEqualToIgnoringWhitespace("1903");

        Assertions.assertThat(myTeam.getName())
                .isEqualToIgnoringWhitespace("Beşiktaş");
    }

    @Test
    public void testShouldStepVerifyTeams() throws Exception {
        Team myTeam = new Team("1903", "Beşiktaş");

        Flux<Team> publisher = Flux.just(myTeam.getId(), myTeam.getName())
                .map(s -> new Team("", s));

        StepVerifier.create(publisher)
                .expectNext(new Team("", "1903"))
                .expectNext(new Team("", "Beşiktaş"))
                .expectComplete()
                .log()
                .verify();

    }

    @Test
    public void testShouldStepVerifyWithVirtualTime() {
        Supplier<Flux<Long>> fluxSupplier = () ->
                Flux.interval(Duration.ofMillis(10000))
                        .map(n -> n+1)
                        .take(1);

        StepVerifier.withVirtualTime(fluxSupplier)
                .expectSubscription() // t =0
                .expectNextCount(0)   // t = 0
                .thenAwait(Duration.ofHours(2)) // t = 2 hours later...
                .expectNext(1L)  // t = 2 hours later... , data exists
                .verifyComplete();
    }
}