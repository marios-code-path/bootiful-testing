package com.example.streamy

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import org.mockito.Mockito
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.BodyInserters

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


    @BeforeAll
    fun setUp() {
        BDDMockito
                .given(messageService.get(anyObject()))
                .willReturn(Flux.just(Message(123456L, "Mario", "Demo Time")))

        BDDMockito
                .given(messageService.put(anyObject(), anyObject(), anyObject()))
                .willReturn(Mono.just(123456L))
    }

    @Test
    fun `should write a message`() {
        WebTestClient
                .bindToRouterFunction(MessageRouters(messageService).routeToAppendStream())
                .build()
                .post()
                .uri("/write")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(MessageRouters.MyRequest("Mario", "Demo Time")), MessageRouters.MyRequest::class.java)
                .exchange()
                .expectStatus().isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .equals(123456L)


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