package com.demo.javafest.service

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.time.Duration

@DataMongoTest
class ServiceApplicationTests {

    @Autowired
    private lateinit var repo: ReactiveMongoRepository<Message, Long>

    @Test
    fun `should save and find`() {
        val message = Message(1L, "Mario", "Luigi", "Hello")

        val writeStream = repo.save(message)
        val readStream = repo.findAll()

        val composed = Flux
                .from(writeStream)
                .thenMany(readStream)

        StepVerifier
                .create(composed)
                .assertNext {
                    Assertions
                            .assertThat(it)
                            .hasNoNullFieldsOrProperties()
                            .hasFieldOrPropertyWithValue("id", 1L)
                }
                .verifyComplete()
    }

}
