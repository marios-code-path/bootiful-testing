package com.example.consumer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.reactive.function.client.WebClient
import java.util.*

@SpringBootApplication
class ConsumerApplication {
	@Bean
	fun webClient(): WebClient = WebClient.builder().build()
}

fun main(args: Array<String>) {
	runApplication<ConsumerApplication>(*args)
}

data class Message(val id: UUID, val text: String)