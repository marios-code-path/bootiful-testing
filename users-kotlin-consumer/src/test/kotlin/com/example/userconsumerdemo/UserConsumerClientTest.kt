package com.example.userconsumerdemo

import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.junit4.SpringRunner
import reactor.test.StepVerifier


//@ExtendWith(SpringExtension::class)  // DOESNGT WORK FOR ME
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureStubRunner(ids = ["com.example:users-kotlin-producer:+:8090"],
        stubsMode = StubRunnerProperties.StubsMode.LOCAL)
@Import(UserClient::class, UserConsumerApp::class)
class UserConsumerClientTest {
    @Autowired
    private lateinit var userClient: UserClient

    private val generalUserMatcher = Matchers.allOf(
            Matchers.notNullValue(),
            Matchers.hasProperty("name", Matchers.notNullValue()),
            Matchers.hasProperty("id", Matchers.notNullValue())
    )!!

    @Test
    fun `should consume service all endpoint`() {
        //val users: Supplier<Publisher<User>> = Supplier { userClient.getAll() }
        StepVerifier
                .create(userClient.getAll())
                .expectSubscription()
                .assertNext { Assert.assertThat(it, generalUserMatcher) }
                .assertNext { Assert.assertThat(it, generalUserMatcher) }
                .verifyComplete()
    }

}
