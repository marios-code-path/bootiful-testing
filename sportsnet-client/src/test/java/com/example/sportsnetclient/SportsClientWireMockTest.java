package com.example.sportsnetclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collection;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureJson
@AutoConfigureWireMock(port = 8080)
public class SportsClientWireMockTest {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    SportsNetClient sportsNetClient;

    @Before
    public void setUp() throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(Arrays.asList(
                new Team(1L, "PADRES")
        ));

        WireMock.stubFor(WireMock.get("/team/all")
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .withBody(json)));
    }

    @Test
    public void testShouldGetTeams() {
        Collection<Team> teams = sportsNetClient.getAllTeams();

        Assertions.assertThat(teams)
                .as("Teams response has team data.")
                .isNotNull()
                .isNotEmpty();

    }

}
