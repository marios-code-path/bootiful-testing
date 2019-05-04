package com.demo.istanbul.producer

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import java.time.Instant
import java.util.*

@WebFluxTest
@ExtendWith(SpringExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRestTests {

    @MockBean
    lateinit var repo: DemoUserRepo

    @BeforeEach
    fun setUp() {
        Mockito.`when`(repo.findAll())
                .thenReturn(
                        Flux.just(DemoUser(UUID.randomUUID(), "Mario", Instant.now()))
                )
    }

    @Test
    fun `should HTTP GET all DemoUser`() {
        WebTestClient
                .bindToRouterFunction(DemoUserRestConfig(repo).demoRoutes())
                .build()
                .get()
                .uri("/all")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.[0].id").isNotEmpty
                .jsonPath("$.[0].name").isEqualTo("Mario")
    }
}