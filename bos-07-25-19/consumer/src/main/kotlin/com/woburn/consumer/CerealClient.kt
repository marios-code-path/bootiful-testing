package com.woburn.consumer

import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import java.util.*

data class Cereal(val id: UUID, val name: String)

@Component
class CerealClient(val client: WebClient) {

    fun getAll(): Flux<Cereal> =
        client
                .get()
                .uri("http://localhost:8091/all")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .retrieve()
                .bodyToFlux(Cereal::class.java)


}
