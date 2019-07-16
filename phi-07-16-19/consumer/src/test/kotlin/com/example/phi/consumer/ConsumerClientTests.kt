package com.example.phi.consumer

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties
import reactor.test.StepVerifier

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureStubRunner(ids = ["com.example.phi:demo:+:8090"], stubsMode = StubRunnerProperties.StubsMode.LOCAL)
class ConsumerClientTests {

    @Autowired
    private lateinit var client: NoteClient

    @Test
    fun `should get all`() {
        StepVerifier
                .create(client.getAll())
                .expectSubscription()
                .assertNext {
                    Assertions
                            .assertThat(it)
                            .isNotNull
                            .hasFieldOrPropertyWithValue("text", "My Note")
                }
                .verifyComplete()

    }
}