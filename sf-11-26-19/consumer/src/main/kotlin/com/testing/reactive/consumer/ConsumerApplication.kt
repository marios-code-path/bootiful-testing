package com.testing.reactive.consumer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.reactive.function.client.WebClient

@SpringBootApplication
class ConsumerApplication {

	@Bean
	fun webClient() = WebClient.builder().build()

	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			runApplication<ConsumerApplication>(*args)
		}
	}
}