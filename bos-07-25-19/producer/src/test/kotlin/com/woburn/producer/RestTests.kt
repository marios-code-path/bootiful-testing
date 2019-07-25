package com.woburn.producer

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import java.util.*

@WebFluxTest
class WebTests {

    @MockBean
    private lateinit var repo: ReactiveMongoRepository<Cereal, UUID>

    @BeforeEach
    fun setUp() {
        BDDMockito
                .given(repo.findAll())
                .willReturn(Flux.just(Cereal(UUID.randomUUID(), "Lucky Charms!")))
    }

    @Test
    fun `should find all`() {
        WebTestClient
                .bindToRouterFunction(CerealBox(repo).routes())
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
                .jsonPath("$.[0].name").isEqualTo("Lucky Charms!")
                .jsonPath("$.[0].id").isNotEmpty
    }
}