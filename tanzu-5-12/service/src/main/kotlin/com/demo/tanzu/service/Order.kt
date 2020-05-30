package com.demo.tanzu.service

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import java.util.*

data class Order(val id: UUID, val item: String, val qty: Int)

interface OrderRepositoryMongoDB : ReactiveMongoRepository<Order, UUID>
