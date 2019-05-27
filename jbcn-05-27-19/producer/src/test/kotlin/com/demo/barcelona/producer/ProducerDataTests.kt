package com.demo.barcelona.producer

import com.datastax.driver.core.utils.UUIDs
import org.assertj.core.api.Assertions
import org.cassandraunit.spring.CassandraDataSet
import org.cassandraunit.spring.CassandraUnit
import org.cassandraunit.spring.CassandraUnitDependencyInjectionTestExecutionListener
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = [ProducerApplication::class])
@CassandraUnit
@TestExecutionListeners(
        CassandraUnitDependencyInjectionTestExecutionListener::class,
        DependencyInjectionTestExecutionListener::class)
@CassandraDataSet("keyspace.cql")
class ProducerDataTests {

    @Autowired
    private lateinit var repo: StarRepository

    @Test
    fun `data should save and find`() {
        val vegaId: UUID = UUIDs.timeBased()
        val star = Star(vegaId, "Vega", 0.03)

        val savePublisher = repo.save(star)

        val findPublisher = repo.findById("Vega")

        val publisher = Flux
                .from(savePublisher)
                .then(findPublisher)

        StepVerifier
                .create(publisher)
                .expectSubscription()
                .assertNext {
                    Assertions
                            .assertThat(it)
                            .isNotNull
                            .hasNoNullFieldsOrProperties()
                            .hasFieldOrPropertyWithValue("id", vegaId)
                            .hasFieldOrPropertyWithValue("name", "Vega")
                            .hasFieldOrPropertyWithValue("magnitude", 0.03)
                }
                .expectComplete()
                .verify()
    }
}