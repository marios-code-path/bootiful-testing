package com.sportsnet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.function.Supplier;

@DataMongoTest
@RunWith(SpringRunner.class)
public class TeamRepositoryTest {

    @Autowired
    private TeamRepository repo;

    private final Team one = new Team("1", "BLUES");
    private final Team two = new Team("2", "REDS");

    @Before
    public void enableFluxDebug() {
        Hooks.onOperatorDebug();
    }

    @Test
    public void testFindAllWithVirtualTime() {
        Supplier<Publisher<Team>> setup = () ->
                this.repo
                        .deleteAll()
                        .checkpoint("findAllWithVirtualTime")
                        .thenMany(this.repo.saveAll(Flux.just(this.one, this.two)))
                        .thenMany(repo.findAll())
                        .delayElements(Duration.ofSeconds(5));

        StepVerifier.withVirtualTime(setup)
                .thenAwait(Duration.ofSeconds(5))
                .expectNextMatches(r -> r.getName().equalsIgnoreCase("BLUES"))
                .expectNoEvent(Duration.ofSeconds(5))
                .expectNextMatches(r -> r.getName().equalsIgnoreCase("REDS"))
                .expectComplete()
                .verify();
    }

    @Test
    public void testShouldFetchByName() {

        Publisher<Team> setup =
                this.repo
                        .deleteAll()
                        .checkpoint("saveAllTeams")
                        .thenMany(this.repo.saveAll(Flux.just(this.one, this.two)));

        Publisher<Team> find = this.repo.findByName("BLUES");

        Publisher<Team> composite = Flux
                .from(setup)
                .thenMany(find);

        StepVerifier
                .create(composite)
                .expectNext(this.one)
                .verifyComplete();
    }

    @Test
    public void testShouldFetchFavorites() {
        Publisher<Team> setup =
                this.repo
                        .deleteAll()
                        .thenMany(this.repo.saveAll(Flux.just(this.one, this.two)));

        Publisher<Team> find = this.repo.getMyFavorites();

        Publisher<Team> composite = Flux
                .from(setup)
                .thenMany(find);

        StepVerifier
                .create(composite)
                .expectNext(this.one, this.two)
                .verifyComplete();
    }
}