package com.example.producer

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import java.util.*

interface MessageRepository : ReactiveMongoRepository<Message, UUID>