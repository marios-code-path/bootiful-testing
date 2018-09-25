package com.example.sportsnet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
@Import({SportsNetClientApp.class, SportsNetClient.class})
@AutoConfigureStubRunner(ids = "com.example.sportsnet:reactive-producer:+:8089",
        stubsMode = StubRunnerProperties.StubsMode.LOCAL)
public class SportsNetClientContractTests {
    @Autowired
    SportsNetClient client;

    @Test
    public void testShouldFetchTeams() {
        Flux<Team> customers = this.client.getAllTeams();
        StepVerifier
                .create(customers)
                .expectNext(new Team("1", "REDS"))
                .expectNext(new Team("2", "BLUES"))
                .verifyComplete();
    }

}