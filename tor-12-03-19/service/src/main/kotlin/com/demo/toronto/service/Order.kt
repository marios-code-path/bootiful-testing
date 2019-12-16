package com.demo.toronto.service

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import java.util.*

data class Order<K>(val id: K, val item: String, val qty: Int)
interface OrderRepo<K> : ReactiveMongoRepository<Order<K>, K>

@Configuration
class OrderController<K>(val repo: OrderRepo<K>) {
    @Bean
    fun route(): RouterFunction<ServerResponse> = router {
        GET("all") {
            ServerResponse
                    .ok()
                    .body(repo.findAll(), Order::class.java)
        }
    }
}