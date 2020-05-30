package com.demo.javafest.service

import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@Component
class WebRouter(val repo: ReactiveMongoRepository<Message, Long>) {
    @Bean
    fun route() : RouterFunction<ServerResponse> = router {
        GET("/all") {
            ServerResponse
                    .ok()
                    .body(repo.findAll(), Message::class.java)
        }
    }
}
