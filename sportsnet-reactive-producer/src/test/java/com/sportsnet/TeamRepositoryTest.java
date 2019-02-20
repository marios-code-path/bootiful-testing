package com.sportsnet;

import java.time.Duration;
import java.util.function.Supplier;
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

  @Autowired private TeamRepository repo;

  private final Team dodgers = new Team("1883", "Dodgers");
  private final Team redsox = new Team("1912", "RedSox");

  @Before
  public void enableFluxDebug() {
    Hooks.onOperatorDebug();
  }

  @Test
  public void testShouldFetchByName() {
    org.springframework.cglib.core.KeyFactory kf;

    Publisher<Team> setup =
        this.repo
            .deleteAll()
            .checkpoint("saveAllTeams")
            .thenMany(this.repo.saveAll(Flux.just(this.dodgers, this.redsox)));

    Publisher<Team> find = this.repo.findByName("Dodgers");

    Publisher<Team> composite = Flux.from(setup).thenMany(find);

    StepVerifier.create(composite).expectNext(this.dodgers).verifyComplete();
  }

  @Test
  public void testShouldFetchFavorites() {
    Publisher<Team> setup =
        this.repo.deleteAll().thenMany(this.repo.saveAll(Flux.just(this.dodgers, this.redsox)));

    Publisher<Team> find = this.repo.getMyFavorites();

    Publisher<Team> composite = Flux.from(setup).thenMany(find);

    StepVerifier.create(composite).expectNext(this.dodgers, this.redsox).verifyComplete();
  }

  @Test
  public void testFindAllWithVirtualTime() {
    Supplier<Flux<Team>> setupSupplier =
        () ->
            this.repo
                .deleteAll()
                //                        .then(Mono.error(new Exception("UHOH")))
                //                        .checkpoint("MYCHECKPOINT")
                .thenMany(this.repo.saveAll(Flux.just(this.dodgers, this.redsox)))
                .thenMany(repo.findAll())
                .delayElements(Duration.ofSeconds(5));

    StepVerifier.withVirtualTime(setupSupplier)
        .expectSubscription()
        .expectNoEvent(Duration.ofSeconds(5))
        .expectNextMatches(team -> team.getName().equalsIgnoreCase("dodgers"))
        .thenAwait(Duration.ofSeconds(5)) // t = 10
        .expectNextMatches(team -> team.getName().equalsIgnoreCase("redsox"))
        .expectComplete()
        .verify(Duration.ofSeconds(5));
  }
}
