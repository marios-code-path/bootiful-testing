package com.sportsnet;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.io.ByteArrayInputStream;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Slf4j
public class TeamTest {

    private final Team red = new Team("1912", "RedSox");
    private final Team blue = new Team("1883", "Dodgers");

    @Test
    public void testShouldCreate() {

        Assert.assertNotNull(red.getName());
        Assert.assertNotSame(new Team("1912", "RedSox"), red);

        Assert.assertThat(red.getId(), Matchers.allOf(
                Matchers.notNullValue(),
                Matchers.equalTo("1912")
        ));

        Assertions.assertThat(red.getName())
                .as("Name exists")
                .isNotNull()
                .isNotBlank()
                .isEqualToIgnoringCase("redsox");
    }

    private final List<String> teamNames = Arrays.asList(
            "RedSox",
            "Dodgers",
            "Padres",
            "Angels"
    );

    @Test
    public void testFluxShouldExecWith() {
        Flux<Team> teamFlux = Flux.just(red, blue);

        StepVerifier.
                create(teamFlux)
                .expectNext(red)
                .expectNext(blue)
                .verifyComplete();
    }

}