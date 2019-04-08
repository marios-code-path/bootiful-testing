package com.demo.producer

import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.Instant
import java.util.*

@DataMongoTest
@ExtendWith(SpringExtension::class)
class UserDataTests {

    @Autowired
    lateinit var repo: ReactiveMongoRepository<User, UUID>

    @Test
    fun `should publish user`() {
        val userMono = Mono
                .just(User(UUID.randomUUID(), "Mario", Instant.now()))

        StepVerifier
                .create(userMono)
                .expectSubscription()
                .assertNext {
                    assertAll("User State",
                            { assertNotNull(it) },
                            { assertEquals("Mario", it.name) },
                            { assertTrue(Instant.now() > it.modified) })
                }
                .expectComplete()
                .verify()
    }

    @Test
    fun `should save and retrieve user`() {
        val id = UUID.randomUUID()

        val userMono = Mono
                .just(User(id, "Mario", Instant.now()))

        val opsPublisher = repo
                .saveAll(userMono)
                .then(
                        repo.findById(id)
                )

        StepVerifier
                .create(opsPublisher)
                .expectSubscription()
                .assertNext {
                    MatcherAssert
                            .assertThat("User was found", it,
                                    Matchers.allOf(
                                            Matchers.hasProperty("id", Matchers.notNullValue()),
                                            Matchers.hasProperty("id", Matchers.equalTo(id)),
                                            Matchers.hasProperty("name", Matchers.equalTo("Mario"))
                                    ))
                }
                .verifyComplete()
    }

}