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

@DataMongoTest
@RunWith(SpringRunner.class)
public class TeamRepositoryTest {

    @Autowired
    private TeamRepository repo;

    private final Team one = new Team("1", "BLUES");
    private final Team two = new Team("2", "REDS");
    private final Team falsity = new Team("3", "REDS");

    @Before
    public void enableFluxDebug() {
        Hooks.onOperatorDebug();
    }

    @Test
    public void testShouldFetchAllTeams() {

        Publisher<Team> setup =
                this.repo
                        .deleteAll()
                        .checkpoint("saveAllTeams")
                        .thenMany(this.repo.saveAll(Flux.just(this.one, this.two)));

        Publisher<Team> find = this.repo.findAll();

        Publisher<Team> composite = Flux
                .from(setup)
                .thenMany(find);

        StepVerifier
                .create(composite)
                .expectNext(this.one, this.two)
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