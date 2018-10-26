package com.sportsnet;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.io.ByteArrayInputStream;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
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

    @Test
    public void testFluxShouldOperate() {   // Dear - give me functional assertions
        Flux<Team> teamFlux = Flux.just(red)
                .map(t -> blue);

        StepVerifier
                .create(teamFlux)
                .consumeNextWith(t -> Assertions.assertThat(t)
                    .as("blue team instead")
                    .isEqualTo(blue)
                )
                .expectComplete()
                .verify();
    }

}