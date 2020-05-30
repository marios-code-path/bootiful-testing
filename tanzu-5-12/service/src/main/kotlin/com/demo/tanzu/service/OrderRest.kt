package com.demo.tanzu.service

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import java.util.*

@Configuration
class OrderRest(val repo: ReactiveMongoRepository<Order, UUID>) {

    @Bean
    fun orderRouter(): RouterFunction<ServerResponse> =
            router {
                GET("/all") {
                    ServerResponse
                            .ok()
                            .body(repo.findAll(), Order::class.java)
                }
            }
}