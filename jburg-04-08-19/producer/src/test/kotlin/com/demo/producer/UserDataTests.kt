package com.demo.producer

import junit.framework.Assert.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.Instant
import java.util.*

@DataMongoTest
@ExtendWith(SpringExtension::class)
class UserDataTests {

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

}