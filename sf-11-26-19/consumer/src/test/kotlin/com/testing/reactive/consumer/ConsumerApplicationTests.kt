package com.testing.reactive.consumer

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties
import reactor.test.StepVerifier
import java.util.function.Consumer

@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment.NONE)
@AutoConfigureStubRunner(ids = ["com.testing.reactive:service:+:8099"], stubsMode = StubRunnerProperties.StubsMode.LOCAL)
class ConsumerApplicationTests {

	@Autowired
    private lateinit var client: OrderClient

    @Test
    fun `should GET all`() {
        StepVerifier
                .create(client.getAll())
				.assertNext {
					Assertions
							.assertThat(it)
							.isNotNull
							.hasNoNullFieldsOrProperties()
							.hasFieldOrPropertyWithValue("item", "COFFEE")
							.hasFieldOrPropertyWithValue("quantity", 2)
				}
				.verifyComplete()
    }
}
