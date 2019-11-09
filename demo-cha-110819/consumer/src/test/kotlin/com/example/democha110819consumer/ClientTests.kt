package com.example.democha110819consumer

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureStubRunner(ids = ["com.example:demo-cha-110819:+:1111"], stubsMode = StubRunnerProperties.StubsMode.LOCAL)
class ClientTests {

	@Test
	fun `should request all from service`() {
		WebTestClient
				.bindToServer()
				.baseUrl("http://localhost:1111/")
				.build()
				.get()
				.uri("/all")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk
				.expectHeader()
				.contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.id").isNotEmpty
				.jsonPath("$.name").isEqualTo("Panthers")
	}
}
