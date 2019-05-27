package com.demo.barcelona.producer

import io.restassured.RestAssured
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.core.publisher.Flux
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import(SpaceRouters::class)
class BaseClassKT {

    @LocalServerPort
    var port: Int? = null

    @MockBean
    lateinit var repo: StarRepository

    val firstUUID: UUID = UUID.fromString("7e43b2fc-59db-11e9-8647-d663bd873d93")
    val secondUUID: UUID = UUID.fromString("7e43b2fd-59db-11e9-8647-d663bd873d93")

    @BeforeEach
    fun setUp() {
        Mockito
                .`when`(repo.findAll())
                .thenReturn(
                        Flux.just(
                                Star(firstUUID, "Vega", 0.03),
                                Star(secondUUID, "Castor", 1.962)
                        )
                )
        RestAssured.baseURI = "http://localhost:$port"
    }
}