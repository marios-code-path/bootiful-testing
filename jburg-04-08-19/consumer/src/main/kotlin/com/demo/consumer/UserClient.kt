package com.demo.consumer

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import java.time.Instant
import java.util.*

data class User(val id: UUID, val name: String, val modified: Instant)

@Component
class UserClient(@Autowired
                 private val webClient: WebClient) {

    fun getAll(): Flux<User> {
        return webClient
                .get()
                .uri("http://localhost:8090/all")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .flatMapMany { r -> r.bodyToFlux(User::class.java) }
    }
}