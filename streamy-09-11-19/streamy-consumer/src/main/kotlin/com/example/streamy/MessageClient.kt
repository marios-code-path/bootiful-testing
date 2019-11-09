package com.example.streamy

import java.util.*

data class Message(val id: UUID, val from: String, val text: String)
class MessageClient {
    fun getAll(topic: String) : Flux<Message> {

    }
}
