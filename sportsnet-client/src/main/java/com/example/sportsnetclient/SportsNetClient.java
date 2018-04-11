package com.example.sportsnetclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;

@Component
public class SportsNetClient {

    private final RestTemplate restTemplate;


    public SportsNetClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${server.url:http://localhost:8080/players}")
    String url;

    public Collection<Player> getAllPlayers() {
        return restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<Collection<Player>>() {
                })
                .getBody();

    }
}
