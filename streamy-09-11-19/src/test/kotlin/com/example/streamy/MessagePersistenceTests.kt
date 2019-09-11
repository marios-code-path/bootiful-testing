package com.example.streamy

import org.assertj.core.api.Assertions
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.core.publisher.Hooks
import reactor.test.StepVerifier
import redis.embedded.RedisServer
import java.io.File

@ExtendWith(SpringExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MessagePersistenceTests {

    val host = "127.0.0.1"
    val port = 7999

    private lateinit var redisServer: RedisServer

    private lateinit var lettuce: LettuceConnectionFactory

    private lateinit var template: ReactiveStringRedisTemplate

    @BeforeAll
    fun setUp() {
        redisServer = RedisServer(File("/usr/local/bin/redis-server"), port)

        redisServer.start()

        lettuce = LettuceConnectionFactory(RedisStandaloneConfiguration(host, port))

        lettuce.afterPropertiesSet()

        ReactiveStringRedisTemplate(lettuce)
                .connectionFactory.reactiveConnection
                .serverCommands().flushAll()
                .block()

        template = ReactiveStringRedisTemplate(lettuce)

        Hooks.onOperatorDebug()
    }

    @AfterAll
    fun tearDown() = redisServer.stop()

    @Test
    fun `should write then read from message service`() {
        val streamKey = "ReadWriteTest"

        val service = MessageService(template)

        val writePublisher = service.put(streamKey, "Mario", "Demo Time").checkpoint("write")

        StepVerifier
                .create(writePublisher)
                .expectSubscription()
                .assertNext {
                    Assertions
                            .assertThat(it)
                            .isNotNull()
                            .isGreaterThan(0)
                }
                .expectComplete()
                .verify()

        val readPublisher = service.get(streamKey).checkpoint("read")

        StepVerifier
                .create(readPublisher)
                .expectSubscription()
                .assertNext {

                    MatcherAssert
                            .assertThat(it,
                                    Matchers.allOf(
                                    Matchers.notNullValue(),
                                    Matchers.hasProperty("from"),
                                    Matchers.hasProperty("text",
                                            Matchers.equalTo("Demo Time")))
                            )
                }
                .expectComplete()
                .verify()
    }

    @Test
    fun `should write to message service`() {
        val service = MessageService(template)

        val publisher = service
                .put("DEMO", "Mario", "Demo Time")

        StepVerifier
                .create(publisher)
                .expectSubscription()
                .assertNext {
                    Assertions
                            .assertThat(it)
                            .`as`("A Long was returned.")
                            .isNotNull()
                            .isGreaterThan(0)
                }
                .expectComplete()
                .verify()
    }

}