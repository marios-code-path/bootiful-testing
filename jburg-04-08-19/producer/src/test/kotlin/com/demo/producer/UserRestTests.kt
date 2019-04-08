package com.demo.producer

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import java.time.Instant
import java.util.*

@WebFluxTest
class UserRestTests {

    @Autowired
    private lateinit var testClient: WebTestClient

    val testUser = User(UUID.randomUUID(), "Marvin", Instant.now())

    @Test
    fun `should find all`() {
        testClient
                .get()
                .uri("/all")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.[0].id").isEqualTo(testUser.id.toString())
                .jsonPath("$.[0].name").isEqualTo(testUser.name)
                .jsonPath("$.[0].modified").isEqualTo(testUser.modified.toEpochMilli())
    }
}