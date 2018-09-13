package com.example.sportsnet;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
//@RunWith(SpringRunner.class)
//@Import({SportsNetClientApp.class, SportsNetClient.class})
//@org.springframework.cloud.contract.wiremock.AutoConfigureWireMock(port = 8080)
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
                                        .withBody("[{ \"id\":\"1\", \"name\":\"REDS\"},{ \"id\":\"2\", \"name\":\"BLUES\"}]")
                        )
        );

    }

    //@Test
    public void testShouldFetchTeams() {
        Flux<Team> customers = this.client.getAllTeams();
        StepVerifier
                .create(customers)
                .expectNext(new Team("1", "REDS"))
                .expectNext(new Team("2", "BLUES"))
                .verifyComplete();
    }

}