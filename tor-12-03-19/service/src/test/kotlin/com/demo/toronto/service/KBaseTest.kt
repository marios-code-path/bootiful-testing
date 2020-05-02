package com.demo.toronto.service

import io.restassured.RestAssured
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.web.server.LocalServerPort
import reactor.core.publisher.Flux
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class KBaseClass {
    @LocalServerPort
    private val port = 0

    @MockBean
    private lateinit var repo: OrderRepo<UUID>

    @BeforeEach
    fun setUp() {
        Mockito
                .`when`<Flux<*>>(repo!!.findAll())
                .thenReturn(Flux.just<Order<UUID>>(
                        Order<UUID>(UUID.fromString("7e43b2fc-59db-11e9-8647-d663bd873d93"), "COFFEE", 2)
                ))
        RestAssured.baseURI = "http://localhost:$port/"
    }
}