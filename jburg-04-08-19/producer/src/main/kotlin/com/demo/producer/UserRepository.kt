package com.demo.producer

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import java.util.*

interface UserRepository : ReactiveMongoRepository<User, UUID> {
    fun findAllUsers()
}