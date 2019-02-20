package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class WebConfig {

    @Bean
    public RouterFunction<ServerResponse> route(PersonRepository repo) {
        return RouterFunctions.route(RequestPredicates.GET("/all"),
                req -> ServerResponse.ok()
                        .body(repo.findAll(), Person.class));
    }
}
