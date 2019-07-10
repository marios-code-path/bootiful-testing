package com.example.consumer

import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux

@Component
class MessageClient(val webClient: WebClient) {

    fun getAll(): Flux<Message> =
        webClient
                .get()
                .uri("http://localhost:8091/all")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .flatMapMany {
                    it.bodyToFlux(Message::class.java)
                }


}
