package com.sportsnet;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

import java.util.UUID;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class SportsNetWebConfig {

    @Bean
    RouterFunction<ServerResponse> routes(TeamRepository cr) {
        return route(GET("/teams/all"), r -> ServerResponse.ok().body(cr.findAll(), Team.class));
    }

    // Require when executing stand-alone only!
    //@Bean
    ApplicationRunner runner(TeamRepository cr) {
        return args ->
                cr
                        .saveAll(Flux.just(new Team(UUID.randomUUID().toString(), "REDS"), new Team(UUID.randomUUID().toString(), "BLUES")))
                        .subscribe(d -> System.out.println("saved " + d.toString()));
    }
}
