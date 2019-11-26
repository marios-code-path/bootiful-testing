package com.testing.reactive.service

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import java.util.*

interface OrderRepository : ReactiveMongoRepository<Order, UUID>