package com.demo.capetown.producer

import org.springframework.context.annotation.Bean
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

class DemoUserRoutes(val repo: DemoUserRepo) {

    @Bean
    fun demoRoutes(): RouterFunction<ServerResponse> = router {
        GET("/all") {
            ServerResponse
                    .ok()
                    .body(repo.findAll(), DemoUser::class.java)
        }
    }
}