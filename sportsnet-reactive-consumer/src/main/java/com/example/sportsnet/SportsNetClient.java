package com.example.sportsnet;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public class SportsNetClient {
    @Value("${server.url:http://localhost:8089/teams}")
    String baseUri;

    private final WebClient webClient;

    public SportsNetClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<Team> getAllTeams() {
        return this.webClient
                .get()
                .uri(baseUri + "/all")
                .retrieve()
                .bodyToFlux(Team.class);
    }

    public Flux<Team> getFavorites() {
        return this.webClient
                .get()
                .uri(baseUri + "/favorites")
                .retrieve()
                .bodyToFlux(Team.class);
    }
}