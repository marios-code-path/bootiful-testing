package com.example.producer

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.BDDMockito
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import java.util.*


@WebFluxTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MessageRESTTests {

    @MockBean
    private lateinit var repo: ReactiveMongoRepository<Message, UUID>

    @BeforeAll
    fun `setUp`() {
        BDDMockito
                .given(repo.findAll())
                .willReturn(Flux.just(Message(UUID.randomUUID(), "HELLO")))
    }

    @Test
    fun `should GET all`() {
        WebTestClient
                .bindToRouterFunction(MessageRouters(repo).mainRoutes())
                .build()
                .get()
                .uri("/all")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectStatus()
                .isOk
                .expectBody()
                .jsonPath("$.[0].text").isEqualTo("HELLO")
    }
}