package com.woburn.producer

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@Configuration
class TaxRouters(val repo: TaxRepository) {

    @Bean
    fun routes(): RouterFunction<ServerResponse> = router {
        GET("/all") {
            ServerResponse
                    .ok()
                    .body(repo.findAll(), Tax::class.java)
        }
    }
}
