package com.example.parisproducer

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import java.util.*

@ExtendWith(SpringExtension::class)
@WebFluxTest
class WebRestTests {

    @MockBean
    lateinit var repo: AccordionRepository

    @BeforeEach
    fun setup() {
        Mockito
                .`when`(repo.findAll())
                .thenReturn(Flux.just(
                        Accordion(UUID.randomUUID(), "Marcel"),
                        Accordion(UUID.randomUUID(), "Maria")))
    }

    @Test
    fun `should retrieve all`() {
        WebTestClient
                .bindToRouterFunction(AccordionRouters().getRouter(repo))
                .build()
                .get()
                .uri("/all")
                .exchange()
                .expectStatus()
                .isOk
                .expectBody()
                .jsonPath("$.[1].name").isEqualTo("Maria")
                .jsonPath("$.[0].name").isEqualTo("Marcel")
    }
}