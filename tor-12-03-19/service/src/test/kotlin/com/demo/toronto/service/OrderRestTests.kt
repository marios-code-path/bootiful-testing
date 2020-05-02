package com.demo.toronto.service

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito
import org.mockito.Mockito
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import java.util.*

//@WebFluxTest
@ExtendWith(SpringExtension::class)
class OrderRestTests {

    @MockBean
    lateinit var repo: OrderRepo<UUID>

    @BeforeEach
    fun setUp() {
        BDDMockito
                .given(repo.findAll())
                .willReturn(Flux.just(
                        Order(UUID.randomUUID(), "COFFEE", 2),
                        Order(UUID.randomUUID(), "BearCats", 5)
                ))
    }

    @Test
    fun `should get all`() {
        WebTestClient
                .bindToRouterFunction(OrderController(repo).route())
                .build()
                .get()
                .uri("/all")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.[0].item").isEqualTo("COFFEE")
                .jsonPath("$.[1].item").isEqualTo("BearCats")
    }

}