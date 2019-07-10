package com.example.producer

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.test.StepVerifier
import java.util.*

@DataMongoTest
class DemoApplicationTests {

    @Autowired
    private lateinit var repo: ReactiveMongoRepository<Message, UUID>

    @Test
    fun `Should save and load Entities`() {
        val message = Message(UUID.randomUUID(), "Hello")

        StepVerifier
                .create(repo
                        .save(message)
                        .thenMany(repo.findAll()))
                .expectSubscription()
                .assertNext {
                    Assertions
                            .assertThat(it)
                            .isNotNull
                            .hasNoNullFieldsOrProperties()
                            .hasFieldOrPropertyWithValue("text", "Hello")
                }
                .expectComplete()
                .verify()
    }

}
