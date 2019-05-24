package com.example.parisproducer

import org.assertj.core.api.Assertions
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.*

@ExtendWith(SpringExtension::class)
@DataMongoTest
class ParisProducerApplicationTests {

    @Autowired
    private lateinit var repo: AccordionRepository

    @Test
    fun `should save and find repo`() {

        val stream = repo
                .saveAll(Flux.just(
                        Accordion(UUID.randomUUID(), "Marcel"),
                        Accordion(UUID.randomUUID(), "Maria")))
                .thenMany(
                        repo.findAll()
                )

        StepVerifier
                .create(stream)
                .assertNext {
                    Assertions
                            .assertThat(it)
                            .isNotNull
                            .hasNoNullFieldsOrProperties()
                            .hasFieldOrPropertyWithValue("name", "Maria")
                }
                .assertNext {
                    MatcherAssert
                            .assertThat("That Maria is present", it,
                                    Matchers.allOf(
                                            Matchers.notNullValue(),
                                            Matchers.hasProperty("name", Matchers.allOf(
                                                    Matchers.notNullValue(),
                                                    Matchers.equalTo("Marcel")
                                            ))
                                    ))
                }
                .verifyComplete()

    }

    @Test
    fun `should carry state through Flux via StepVerifier`() {
        val accordion = Accordion(UUID.randomUUID(), "Marcel")

        StepVerifier
                .create(Mono.just(accordion))
                .assertNext {
                    Assertions
                            .assertThat(it)
                            .isNotNull
                            .hasNoNullFieldsOrProperties()
                }
                .verifyComplete()
    }


}
