package com.demo.capetown.producer

import org.assertj.core.api.Assertions
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.Instant
import java.util.*

@DataMongoTest
@ExtendWith(SpringExtension::class)
class UserPersistenceTests {

    @Autowired
    lateinit var repoDemo: DemoUserRepo

    @Test
    fun `should stream user and verify`() {
        val userPublisher = Mono
                .just(DemoUser(UUID.randomUUID(), "Mario", Instant.now()))

        StepVerifier
                .create(userPublisher)
                .expectSubscription()
                .assertNext {
                    Assertions
                            .assertThat(it)
                            .isNotNull
                            .hasNoNullFieldsOrProperties()
                            .hasFieldOrPropertyWithValue("name", "Mario")
                }
                .verifyComplete()
    }

    @Test
    fun `should persist and find`() {
        val persistencePublisher = repoDemo
                .save(DemoUser(UUID.randomUUID(), "Mario", Instant.now()))
                .thenMany(repoDemo.findAll())

        StepVerifier
                .create(persistencePublisher)
                .expectSubscription()
                .assertNext {
                    MatcherAssert.assertThat("DemoUser has State", it,
                            Matchers.allOf(
                                    Matchers.notNullValue(),
                                    Matchers.hasProperty("name", Matchers.equalTo("Mario"))
                            ))
                }
                .expectNextCount(0)
                .verifyComplete()

    }
}