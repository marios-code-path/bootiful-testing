package com.demo.producer

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.util.*

@DataMongoTest
@ExtendWith(SpringExtension::class)
class ProducerApplicationTests {

    @Autowired
    private lateinit var repo: PeachRepository

    @Test
    fun `should save and find peach`() {
        val peach = Peach(UUID.randomUUID(), "Large")

        val saveStream = repo.save(peach)

        val combined = Flux
                .from(saveStream)
                .thenMany(repo.findAll())

        StepVerifier
                .create(combined)
                .expectSubscription()
                .assertNext {
                    Assertions
                            .assertThat(it)
                            .isNotNull
                            .hasNoNullFieldsOrProperties()
                            .hasFieldOrProperty("id")
                            .hasFieldOrPropertyWithValue("size", "Large")
                }
                .expectComplete()
                .verify()
    }
}
