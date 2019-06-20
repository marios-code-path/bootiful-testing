package com.demo.capetown.consumer

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import java.time.Instant
import java.util.*

data class DemoUser(val id: UUID, val name: String, val modified: Instant)

@Component
class DemoClient(@Autowired
                 private val webClient: WebClient) {

    fun getAll(): Flux<DemoUser> {
        return webClient
                .get()
                .uri("http://localhost:8090/all")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .flatMapMany {
                    it.bodyToFlux(DemoUser::class.java)
                }
    }
}