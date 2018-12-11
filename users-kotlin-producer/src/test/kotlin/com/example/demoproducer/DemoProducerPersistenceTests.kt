package com.example.demoproducer

import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.core.publisher.Flux
import reactor.test.StepVerifier

@DataMongoTest
@ExtendWith(SpringExtension::class)
class DemoProducerPersistenceTests {

    @Autowired
    private lateinit var repo: UserRepository

    private val consistencyMatcher = Matchers.allOf(
            Matchers.hasProperty("id", Matchers.notNullValue()),
            Matchers.hasProperty("name", Matchers.notNullValue())
    )!!

    @Test
    fun `shouldtestThatDataPersistenceWorks`() {
        val user = User("1234", "Mario")

        val setupPublisher = repo
                .deleteAll()
                .then(repo.save(user))

        val composite = Flux
                .from(setupPublisher)
                .thenMany(repo.findAll())

        StepVerifier
                .create(composite)
                .expectSubscription()
                .assertNext {
                    MatcherAssert.assertThat("State should be consistent",
                            it,
                            consistencyMatcher)
                }
                .`as`("Consistence Check")
                .verifyComplete()
    }
}