package com.demo.istanbul.consumer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.reactive.function.client.WebClient

@SpringBootApplication
class IstanbulConsumerApp {
	@Bean
	fun webClient() = WebClient.builder().build()
}

fun main(args: Array<String>) {
	runApplication<IstanbulConsumerApp>(*args)
}
