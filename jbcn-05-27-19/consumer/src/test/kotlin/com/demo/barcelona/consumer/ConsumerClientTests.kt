package com.demo.barcelona.consumer

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.test.StepVerifier


@ExtendWith(SpringExtension::class)
@SpringBootTest
@AutoConfigureStubRunner(ids = ["com.demo.barcelona:producer:+:8090"], stubsMode = StubRunnerProperties.StubsMode.LOCAL)
class ConsumerClientTests {
    @Autowired
    private lateinit var client: Client

    @Test
    fun `should find all`() {
        StepVerifier
                .create(client.getAll())
                .expectSubscription()
                .assertNext {
                    org.assertj.core.api.Assertions
                            .assertThat(it)
                            .isNotNull
                            .hasNoNullFieldsOrProperties()
                            .hasFieldOrPropertyWithValue("name", "Vega")
                }
                .assertNext {
                    org.assertj.core.api.Assertions
                            .assertThat(it)
                            .isNotNull
                            .hasNoNullFieldsOrProperties()
                            .hasFieldOrPropertyWithValue("name", "Castor")
                }
                .verifyComplete()
    }
}