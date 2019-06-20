package com.demo.producer

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import java.util.*

@ExtendWith(SpringExtension::class)
@WebFluxTest
@Import(PeachRouters::class)
class ProducerRESTTests {
    @MockBean
    private lateinit var repo: PeachRepository

    @Test
    fun `should getall`() {
        BDDMockito
                .given(repo.findAll())
                .willReturn(Flux.just(
                        Peach(UUID.randomUUID(), "SUPER")
                ))

        WebTestClient
                .bindToRouterFunction(PeachRouters().route(repo))
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
                .jsonPath("$.[0].size").isEqualTo("SUPER")

    }
}