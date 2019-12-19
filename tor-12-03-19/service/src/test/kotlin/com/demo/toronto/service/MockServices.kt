package com.demo.toronto.service

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.*
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.Duration

class OrderPersistTests {

    @Mock
    private  lateinit var mongoService: OrderPersistenceMongo<Int>

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `should save an order`() {
        whenever(mongoService.addOrder(any()))
                .thenReturn(Mono.empty())

        StepVerifier
                .create(mongoService.addOrder(Order(1, "Test", 5)))
                .expectSubscription()
                .expectComplete()
                .verify(Duration.ofSeconds(1))
    }
}