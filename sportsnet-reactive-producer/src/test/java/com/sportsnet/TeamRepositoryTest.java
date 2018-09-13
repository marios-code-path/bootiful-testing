package com.sportsnet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@DataMongoTest
@RunWith(SpringRunner.class)
public class TeamRepositoryTest {

    @Autowired
    private TeamRepository repo;

    private final Team one = new Team("1", "BLUES");
    private final Team two = new Team("2", "REDS");

    @Test
    public void query() {

        Publisher<Team> setup = this.repo
                .deleteAll()
                .thenMany(this.repo.saveAll(Flux.just(this.one, this.two)));

        Publisher<Team> all = this.repo.findAll();

        Publisher<Team> composite = Flux
                .from(setup)
                .thenMany(all);

        StepVerifier
                .create(composite)
                .expectNext(this.one, this.two)
                .verifyComplete();
    }
}