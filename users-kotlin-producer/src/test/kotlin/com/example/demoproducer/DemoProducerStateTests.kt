package com.example.demoproducer

import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.function.Executable
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import org.assertj.core.api.Assertions as Assertj

@ExtendWith(SpringExtension::class)
class DemoProducerStateTests {

    @Test
    fun `testShouldNeverTestTrivialCode`() {
        val user = User("1234", "Mario")

        Assertions.assertNotSame(user, User("1234", "Mario"))
        Assertions.assertAll("Should have constructed state",
                Executable { Assertions.assertEquals("Mario", user.name) },
                Executable { Assertions.assertEquals("1234", user.id) }
        )

        val matcher = Matchers.allOf(
                Matchers.hasProperty("id", Matchers.equalTo("1234")),
                Matchers.hasProperty("name", Matchers.equalTo("Mario"))
        )

        MatcherAssert.assertThat("Hamcrest Matchers validate Object state",
                user, matcher)

        Assertj.assertThat(user).`as`("Object state is consistent")
                .isNotNull
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("id", "1234")
                .hasFieldOrPropertyWithValue("name", "Mario")
                .isNotSameAs(User("1234", "Mario"))
    }

    @Test
    fun `shouldNeverTestTrivialCodeEvenReactively`() {
        val user = User("1234", "Mario")

        val matcher = Matchers.allOf(
                Matchers.hasProperty("id", Matchers.equalTo("1234")),
                Matchers.hasProperty("name", Matchers.equalTo("Mario"))
        )

        StepVerifier
                .create(Flux.just(user))
                .expectSubscription()
                .assertNext { MatcherAssert.assertThat("Consistend object state",
                        it, matcher) }
                .verifyComplete()
    }
}