package com.sportsnet;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Date;


public class TeamTest {

    private final Team myTeam = new Team("1903", "Beşiktaş");

    @Test
    public void create() throws Exception {

        Assertions.assertThat(myTeam.getId()).isEqualToIgnoringWhitespace("1903");
        Assertions.assertThat(myTeam.getName()).isEqualToIgnoringWhitespace("Beşiktaş");
    }

    @Test
    public void testShouldStepVerifyTeams() throws Exception {
        StepVerifier.create(Flux.just(myTeam.getId(), myTeam.getName()))
                .expectNext("1903", "Beşiktaş")
                .expectComplete()
                .verifyThenAssertThat()
                .hasNotDroppedElements();

    }

    private int size = 2;

    @Test
    public void works() {
        StepVerifier.withVirtualTime(() -> Flux.interval(Duration.ofMillis(10000))
                .map(tick -> new Date())
                .take(size)
                .collectList()
        )
                .thenAwait(Duration.ofMillis(2000))
                .expectNoEvent(Duration.ofMillis(1000))
                .thenAwait(Duration.ofHours(2))
                .consumeNextWith(list -> Assert.assertTrue(list.size() == size))
                .verifyComplete();
    }
}