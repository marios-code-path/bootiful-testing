package com.demo.barcelona.consumer

import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import java.util.*


data class Star(
        val id: UUID,
        val name: String,
        val magnitude: Double)

@Component
class Client(val webClient: WebClient) {

    fun getAll(): Flux<Star> =
            webClient
                    .get()
                    .uri("http://localhost:8090/all")
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .retrieve()
                    .bodyToFlux(Star::class.java)

}