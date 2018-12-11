package com.example.userconsumerdemo

import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux

@Component
class UserClient(val webClient: WebClient) {
    fun getAll(): Flux<User> {
        return webClient
                .get()
                .uri("http://localhost:8090/all")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .flatMapMany { r -> r.bodyToFlux(User::class.java) }
    }
}
