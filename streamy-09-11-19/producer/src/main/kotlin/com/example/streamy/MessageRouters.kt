package com.example.streamy

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono
import java.util.*

data class MyRequest(val from: String, val text: String)

@Configuration
class MessageRouters(val messageService: MessageService) {

    @Bean
    fun routeToReadStream(): RouterFunction<ServerResponse> = router {
        GET("/read") {
            ServerResponse
                    .ok()
                    .body(messageService.get("demo"), Message::class.java)
        }
    }

    @Bean
    fun routeToAppendStream(): RouterFunction<ServerResponse> = router {
        POST("/append") {
            val serviceResult = it
                    .bodyToMono(MyRequest::class.java)
                    .flatMap { req ->
                        messageService.put("demo", req.from, req.text)
                    }

            ServerResponse
                    .ok()
                    .body(serviceResult, UUID::class.java)
        }
    }
}