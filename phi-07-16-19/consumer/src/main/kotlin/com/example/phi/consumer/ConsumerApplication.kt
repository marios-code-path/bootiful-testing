package com.example.phi.consumer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.reactive.function.client.WebClient

@SpringBootApplication
class ConsumerApplication {
	@Bean
	fun webClient(): WebClient = WebClient.builder().build()
}

fun main(args: Array<String>) {
	runApplication<ConsumerApplication>(*args)
}
