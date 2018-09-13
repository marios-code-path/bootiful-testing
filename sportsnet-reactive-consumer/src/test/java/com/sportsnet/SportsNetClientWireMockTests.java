package com.sportsnet;

import com.example.sportsnet.SportsNetClient;
import com.example.sportsnet.SportsNetClientApp;
import com.example.sportsnet.Team;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
@Import({SportsNetClientApp.class, SportsNetClient.class})
@org.springframework.cloud.contract.wiremock.AutoConfigureWireMock(port = 8080)
public class SportsNetClientWireMockTests {

    @Autowired
    private SportsNetClient client;

    @Before
    public void setupWireMock() {

        WireMock.stubFor(
                WireMock
                        .get("/teams/all")
                        .willReturn(
                                WireMock
                                        .aResponse()
                                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
                                        .withBody("[{ \"id\":\"1\", \"name\":\"BLUES\"},{ \"id\":\"2\", \"name\":\"REDS\"}]")
                        )
        );

    }

    @Test
    public void testShouldFetchTeams() {
        Flux<Team> customers = this.client.getAllTeams();
        StepVerifier
                .create(customers)
                .expectNext(new Team(1L, "REDS"))
                .expectNext(new Team(2L, "BLUES"))
                .verifyComplete();
    }

}