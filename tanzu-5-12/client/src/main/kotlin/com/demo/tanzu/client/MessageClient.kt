package com.demo.tanzu.client

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux

data class Message(val id: Int, val from: String, val to: String, val body: String)

@Component
class MessageClient(val webclient: WebClient) {

    fun getAll(): Flux<Message> = webclient
            .get()
            .uri("http://localhost:8099/all")
            .retrieve()
            .bodyToFlux(Message::class.java)
}
