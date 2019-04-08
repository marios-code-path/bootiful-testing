package com.demo.producer

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import java.time.Instant
import java.util.*

@WebFluxTest
@Import(UserWebConfig::class)
class UserRestTests {

    @Autowired
    private lateinit var testClient: WebTestClient

    @MockBean
    private lateinit var repo: UserRepository

    val testUser = User(UUID.randomUUID(), "Marvin", Instant.now())

    @BeforeEach
    fun setUp() {
        Mockito
                .`when`(repo.findAll())
                .thenReturn(Flux.just(testUser))
    }

    @Test
    fun `basic test`() {
         val uuid = UUID.fromString("7e43b2fc-59db-11e9-8647-d663bd873d93")
         val testInstant = Instant.ofEpochMilli(1554712769711L)
        println("TEST: $testInstant")

    }
    @Test
    fun `should find all`() {
        testClient
                .get()
                .uri("/all")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.[0].id").isEqualTo(testUser.id.toString())
                .jsonPath("$.[0].name").isEqualTo(testUser.name)
                .jsonPath("$.[0].modified").isNotEmpty
    }
}