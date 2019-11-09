package com.example.democha110819

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
class RestTests {

    @MockBean
    lateinit var repo: ReactiveMongoRepository<Team, UUID>

    @BeforeEach
    fun setUp() {
        BDDMockito
                .given(repo.findAll())
                .willReturn(
                        Flux.just(
                                Team(UUID.randomUUID(), "Panthers")
                        )
                )
    }

    @Test
    fun `should get all`() {
        WebTestClient
                .bindToRouterFunction(TeamRouters().route(repo))
                .build()
                .get()
                .uri("/all")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.[0].id").isNotEmpty
                .jsonPath("$.[0].name").isEqualTo("Panthers")
    }
}