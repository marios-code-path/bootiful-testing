package com.testing.reactive.service

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import java.util.*

@WebFluxTest
class ServiceRESTTests {
    @MockBean
    private lateinit var repo: OrderRepository

    @BeforeEach
    fun SetUp() {
        BDDMockito
                .given(repo.findAll())
                .willReturn(Flux.just(
                        Order(UUID.randomUUID(), "COFFEE", 2),
                        Order(UUID.randomUUID(), "BISCOTTI", 4)
                ))
    }

    @Test
    fun `should GET all orders`() {
        WebTestClient
                .bindToRouterFunction(OrderWebRoutes(repo).orderRoute())
                .build()
                .get()
                .uri("/all")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.[0].item").isEqualTo("COFFEE")
                .jsonPath("$.[1].item").isEqualTo("BISCOTTI")
    }
}