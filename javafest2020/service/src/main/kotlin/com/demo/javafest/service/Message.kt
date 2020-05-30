package com.demo.javafest.service

import org.springframework.data.mongodb.repository.ReactiveMongoRepository

data class Message(val id: Long, val from: String, val to: String, val body: String)

interface MessageRepository : ReactiveMongoRepository<Message, Long>