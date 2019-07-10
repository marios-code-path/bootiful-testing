package com.example.consumer

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.test.StepVerifier

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureStubRunner(ids = ["com.example:producer:+:8091"], stubsMode = StubRunnerProperties.StubsMode.LOCAL)
@ExtendWith(SpringExtension::class)
class MessageRestConsumerTests {

    @Autowired
    private lateinit var client : MessageClient

    @Test
    fun testService() {
        StepVerifier
                .create(client.getAll())
                .expectSubscription()
                .assertNext {
                    Assertions
                            .assertThat(it)
                            .isNotNull
                            .hasNoNullFieldsOrProperties()
                            .hasFieldOrPropertyWithValue("text", "HELLO")
                }
                .verifyComplete()
    }
}