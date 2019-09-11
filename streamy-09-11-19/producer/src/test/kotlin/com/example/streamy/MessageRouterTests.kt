package com.example.streamy

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito
import org.mockito.Mockito
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Hooks
import reactor.core.publisher.Mono
import java.util.*

fun <T> anyObject(): T {
    Mockito.anyObject<T>()
    return uninitialized()
}

fun <T> uninitialized(): T = null as T

@ExtendWith(SpringExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MessageRouterTests {

    @MockBean
    private lateinit var messageService: MessageService

    @BeforeEach
    fun setUp() {
        BDDMockito
                .given(messageService.get(anyObject()))
                .willReturn(Flux.just(Message(UUID(123456L,0L), "Mario", "Demo Time")))

        BDDMockito
                .given(messageService.put(anyObject(), anyObject(), anyObject()))
                .willReturn(Mono.just(UUID(123456L,0L)))

        Hooks.onOperatorDebug()
    }

    @Test
    fun `should write a message`() {
        WebTestClient
                .bindToRouterFunction(MessageRouters(messageService).routeToAppendStream())
                .build()
                .post()
                .uri("/append")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(MyRequest("Mario", "Demo Time")), MyRequest::class.java)
                .exchange()
                .expectStatus().isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .equals(UUID(123456L, 0L))


    }

    @Test
    fun `should read a messages`() {
        WebTestClient
                .bindToRouterFunction(MessageRouters(messageService).routeToReadStream())
                .build()
                .get()
                .uri("/read")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.[0].from").isEqualTo("Mario")
    }

}