package com.example.democha110819

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import java.util.*

@Configuration
class TeamRouters {

    @Bean
    fun route(repo: ReactiveMongoRepository<Team, UUID>)
            : RouterFunction<ServerResponse> = router {
        GET("/all") { req ->
            ServerResponse
                    .ok()
                    .body(repo.findAll(), Team::class.java)
        }
    }
}
