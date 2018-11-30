package com.sportsnet;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class SportsNetWebConfig {


    @Bean
    RouterFunction<ServerResponse> routes(TeamRepository repo) {
        return
                route(GET("/teams/all"),
                        req -> ServerResponse
                                .ok()
                                .body(repo.findAll(), Team.class))
                        .and(route(GET("/teams/favorites"),
                                req -> ServerResponse
                                        .ok()
                                        .body(repo.getMyFavorites(), Team.class)))
                        .and(route(GET("/teams/byName"),
                                req -> ServerResponse
                                        .ok()
                                        .body(repo.findByName(req.queryParam("name").orElse("NONE")), Team.class)));
    }
}
