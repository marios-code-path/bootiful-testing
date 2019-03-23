package com.example.producer

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.returnResult
import reactor.core.publisher.Flux
import reactor.core.publisher.Hooks
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.*


@WebFluxTest
@ExtendWith(SpringExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import(CarRestService::class)
class CarHttpTests {

    @MockBean
    lateinit var carService: CarService

    val key = UUID.randomUUID().toString()

    private fun <T> anyObject(): T {
        Mockito.anyObject<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T

    @BeforeEach
    fun setUp() {
        Mockito.`when`(carService.newCar(anyObject()))
                .thenReturn(Mono.just(key))
        Mockito.`when`(carService.getCar(Mockito.anyString()))
                .thenReturn(Mono.just(Car("Tesla", "Z", "Orange")))

        Hooks.onOperatorDebug()
    }

    @Test
    fun `should mock return value`() {
        StepVerifier
                .create(carService
                        .newCar(Car("Tesla", "Z", "red"))
                )
                .expectSubscription()
                .assertNext {
                    org.junit.jupiter.api.assertAll("Car has value",
                            { Assertions.assertNotNull(it) }
                    )
                }
                .verifyComplete()
    }

    @Test
    fun `should get car`() {
        WebTestClient
                .bindToRouterFunction(CarRestService().routes(carService))
                .build()
                .get()
                .uri("/car?key=$key")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("\$.make").isEqualTo("Tesla")
                .jsonPath("\$.model").isEqualTo("Z")
                .jsonPath("\$.color").isEqualTo("Orange")
    }

    @Test
    fun `should new endpoint called and return`() {
        val response: Flux<String> = WebTestClient
                .bindToRouterFunction(CarRestService().routes(carService))
                .build()
                .post()
                .uri("/new?color=blue")
                .exchange()
                .expectStatus().isOk
                .expectHeader()
                .contentType("text/plain;charset=UTF-8")
                .returnResult<String>().responseBody

        StepVerifier
                .create(response)
                .expectSubscription()
                .assertNext(this::stringAssertion)
                .verifyComplete()
    }

    fun stringAssertion(value: String) {
        org.junit.jupiter.api.assertAll("Car has State",
                { assertNotNull(value) },
                { assertEquals(key, value) })
    }
}