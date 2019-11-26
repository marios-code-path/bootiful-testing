package com.testing.reactive.service

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import reactor.test.StepVerifier
import java.time.Duration
import java.util.*

@DataMongoTest
class ServiceApplicationTests {

	@Autowired
	private lateinit var repo: OrderRepository

	@Test
	fun `should save and return 1 order`() {
		val order = Order(UUID.randomUUID(), "Coffeee", 2)

		val publisher = repo
				.save(order)
				.thenMany(repo.findAll())

		StepVerifier
				.create(publisher)
				.assertNext{
					Assertions
							.assertThat(it)
							.isNotNull
							.hasNoNullFieldsOrProperties()
				}
				.expectComplete()
				.verify(Duration.ofSeconds(1))
	}
}
