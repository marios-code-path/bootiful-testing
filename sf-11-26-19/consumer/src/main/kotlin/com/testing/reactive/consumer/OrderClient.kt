package com.testing.reactive.consumer

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import java.util.*

data class Order(val id: UUID, val item: String, val quantity: Int)

@Component
class OrderClient(val client: WebClient) {

    fun getAll(): Flux<Order> =
        client
                .get()
                .uri("http://localhost:8099/all")
                .retrieve()
                .bodyToFlux(Order::class.java)

}
