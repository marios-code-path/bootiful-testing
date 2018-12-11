package com.example.demoproducer

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux

@ExtendWith(SpringExtension::class)
@WebFluxTest            // test  filters at some point :)
class DemoProducerWebTests {

    @MockBean
    private lateinit var repo: UserRepository

    @BeforeEach
    fun `setupAndConfigureMockBeans`() {
        Mockito
                .`when`(repo.findAll())
                .thenReturn(Flux.just(
                        User("1234", "Mario"),
                        User("2345", "Rob")
                ))
    }

    @Test
    fun `shouldTestRESTFindAllEndpoint`() {
        WebTestClient
                .bindToRouterFunction(DemoProducerEndpoint().routes(repo))
                .build()
                .get()
                .uri("/all")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("\$.[0].id").isEqualTo("1234")
                .jsonPath("\$.[1].id").isEqualTo("2345")
                .jsonPath("\$.[0].name").isEqualTo("Mario")
                .jsonPath("\$.[1].name").isEqualTo("Rob")

    }
}