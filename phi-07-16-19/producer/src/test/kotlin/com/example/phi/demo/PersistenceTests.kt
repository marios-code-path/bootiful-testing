package com.example.phi.demo

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import reactor.test.StepVerifier
import java.util.*

@DataMongoTest
class PersistenceTests {

    @Autowired
    private lateinit var repo: NoteRepository
    @Test
    fun `should save load`() {
        val data = Note(UUID.randomUUID(), "My Note")

        StepVerifier
                .create(repo.save(data))
                .expectSubscription()
                .assertNext {
                    Assertions
                            .assertThat(it)
                            .`as`("Notes save all state")
                            .isNotNull
                            .hasFieldOrPropertyWithValue("text", "My Note")
                }
                .verifyComplete()

                }
}
