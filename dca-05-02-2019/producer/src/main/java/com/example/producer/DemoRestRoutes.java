package com.example.producer;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Component
public class DemoRestRoutes {

    private final UserRepository repo;


    public DemoRestRoutes(UserRepository repo) {
        this.repo = repo;
    }

    @Bean
    public RouterFunction<ServerResponse> demoUserRoutes() {
        return RouterFunctions.route(RequestPredicates.GET("/all"),
                req ->
                        ServerResponse
                                .ok()
                                .body(repo.findAll(), DemoUser.class)
        );
    }

}
