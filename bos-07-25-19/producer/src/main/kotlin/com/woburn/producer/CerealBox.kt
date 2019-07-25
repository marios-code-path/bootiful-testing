package com.woburn.producer

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import java.util.*

@Configuration
class CerealBox(val repo: ReactiveMongoRepository<Cereal, UUID>) {

    @Bean
    fun routes(): RouterFunction<ServerResponse> = router {
        GET("/all") {
            ServerResponse
                    .ok()
                    .body(repo.findAll(), Cereal::class.java)
        }
    }
}
