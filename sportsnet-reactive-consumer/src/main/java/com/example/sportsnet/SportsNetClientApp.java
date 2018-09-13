package com.example.sportsnet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class SportsNetClientApp {

    @Bean
    WebClient client() {
        return WebClient.builder().build();
    }

    public static void main(String[] args) {
        SpringApplication.run(SportsNetClientApp.class, args);
    }
}

