package com.example.producer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class UserRoutes {

    @Bean
    public RouterFunction<ServerResponse> route(UserRepository repo) {
        return RouterFunctions.route(RequestPredicates.GET("/all"),
                request -> ServerResponse
                        .ok()
                        .body(repo.findAll(), User.class)
        );
    }

}
