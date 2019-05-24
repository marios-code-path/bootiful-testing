package com.example.parisproducer

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@Configuration
class AccordionRouters {

    @Bean
    fun getRouter(repo: AccordionRepository): RouterFunction<ServerResponse> =
        router {
            GET("/all") {
                ServerResponse
                        .ok()
                        .body(repo.findAll(), Accordion::class.java)
            }
    }

}
