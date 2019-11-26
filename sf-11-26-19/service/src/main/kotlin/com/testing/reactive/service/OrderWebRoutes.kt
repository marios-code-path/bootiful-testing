package com.testing.reactive.service

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@Configuration
class OrderWebRoutes(val repo: OrderRepository) {

    @Bean
    fun orderRoute(): RouterFunction<ServerResponse> = router {
        GET("/all") {
          ServerResponse
                  .ok()
                  .body(repo.findAll(), Order::class.java)
        }
    }
}