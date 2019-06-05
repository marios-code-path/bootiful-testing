package com.demo.producer

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@Configuration
class SpaceRouters {

    @Bean // what is the Kotlin DSL Narrative ?
    fun routes(repo: StarRepository): RouterFunction<ServerResponse> =
            router {
                GET("/all") {
                    ServerResponse
                            .ok()
                            .body(repo.findAll(), Star::class.java)
                }
            }
}
