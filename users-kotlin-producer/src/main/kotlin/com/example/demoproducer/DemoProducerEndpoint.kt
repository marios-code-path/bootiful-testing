package com.example.demoproducer

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.*

@Configuration
class DemoProducerEndpoint {

    @Bean
    fun routes(repo: UserRepository): RouterFunction<ServerResponse> = router {
        GET("/all") {
            ServerResponse
                    .ok()
                    .body(repo.findAll())
        }
    }
}
