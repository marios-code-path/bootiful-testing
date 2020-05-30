package com.demo.tanzu.service

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import java.util.*

@WebFluxTest
@Import(OrderRest::class)
class ServiceRestTests {

   // @Autowired
   ///private lateinit var testClient: WebTestClient

    @MockBean
    private lateinit var repo: ReactiveMongoRepository<Order, UUID>

    val myId = "7e43b2fc-59db-11e9-8647-d663bd873d93"

    @BeforeEach
    fun setUp() {
        BDDMockito
                .given(repo.findAll())
                .willReturn(
                        Flux.just(
                                Order(UUID.fromString(myId), "Valve", 3)
                        )
                )
    }

    @Test
    fun `should GET all`() {
        WebTestClient
                .bindToRouterFunction(OrderRest(repo).orderRouter())
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
                .jsonPath("$.[0].item").isEqualTo("Valve")
                .jsonPath("$.[0].id").isEqualTo(myId)
    }
}