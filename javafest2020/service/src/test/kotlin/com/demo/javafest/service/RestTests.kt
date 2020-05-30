package com.demo.javafest.service

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux

@WebFluxTest
class RestTests {

    @MockBean
    private lateinit var repo: ReactiveMongoRepository<Message, Long>

    @BeforeEach
    fun setUp() {
        BDDMockito
                .given(repo.findAll())
                .willReturn(
                        Flux.just(
                                Message(1L, "Mario", "Luigi", "Pasta"),
                                Message(2L, "Luigi", "Mario", "Meatball")
                        )
                )
    }

    @Test
    fun `should GET all`() {
        WebTestClient
                .bindToRouterFunction(WebRouter(repo).route())
                .build()
                .get()
                .uri("/all")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.[0].id").isEqualTo(1L)
                .jsonPath("$.[0].from").isEqualTo("Mario")

    }


}