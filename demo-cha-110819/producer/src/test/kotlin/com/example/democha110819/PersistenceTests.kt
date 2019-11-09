package com.example.democha110819

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.time.Duration
import java.util.*

@DataMongoTest
class PersistenceTests {

    @Autowired
    private lateinit var repo: ReactiveMongoRepository<Team, UUID>

    @Test
    fun `test should persist and find`() {
        var item = Team(UUID.randomUUID(), "Panthers")

        var stream = repo
                .save(item)

        var composed = Flux
                .from(stream)
                .thenMany(repo.findAll())

        StepVerifier
                .create(composed)
                .expectSubscription()
                .assertNext {
                    Assertions
                            .assertThat(it)
                            .isNotNull
                            .hasNoNullFieldsOrProperties()
                }
                .expectComplete()
                .verify(Duration.ofSeconds(5))
    }


}
