package com.demo.barcelona.producer

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
import java.util.*

@ExtendWith(SpringExtension::class)
@WebFluxTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProducerWebTests {

    @MockBean
    private lateinit var repo: StarRepository

    @BeforeEach
    fun setUp() {
        Mockito
                .`when`(repo.findAll())
                .thenReturn(Flux.just(
                        Star(UUID.randomUUID(), "Vega", 0.03),
                        Star(UUID.randomUUID(), "Castor", 1.962)
                ))
    }

    @Test
    fun `should GET all`() {
        WebTestClient
                .bindToRouterFunction(SpaceRouters().routes(repo))
                .build()
                .get()
                .uri("/all")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus()
                .isOk
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.[0].name").isEqualTo("Vega")
                .jsonPath("$.[1].name").isEqualTo("Castor")
    }
}