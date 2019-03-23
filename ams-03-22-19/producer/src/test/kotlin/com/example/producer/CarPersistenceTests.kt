package com.example.producer

import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest
import org.springframework.context.annotation.Import
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.core.publisher.Flux
import reactor.core.publisher.Hooks
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import redis.embedded.RedisServer
import java.util.*

@DataRedisTest
@ExtendWith(SpringExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import(RedisConfiguration::class)
class CarPersistenceTests {

    private val port = 6777

    private lateinit var redisServer: RedisServer

    private lateinit var lettuce: LettuceConnectionFactory

    private lateinit var template: ReactiveRedisTemplate<String, Car>

    @Autowired
    private lateinit var serBuilder: RedisSerializationContext.RedisSerializationContextBuilder<String, Car>

    @BeforeAll
    fun setupRedis() {
        redisServer = RedisServer(port)

        redisServer.start()

        lettuce = LettuceConnectionFactory(RedisStandaloneConfiguration("127.0.0.1", port))

        lettuce.afterPropertiesSet()

        template = ReactiveRedisTemplate(lettuce, serBuilder.build())

        Hooks.onOperatorDebug()
    }

    @AfterAll
    fun tearDown() = redisServer.stop()

    @Test
    fun testShouldSetGet() {

        val key = UUID.randomUUID().toString()

        val setFlux = template.opsForValue()
                .set(key, Car("Tesla", "Model Z", "red"))

        val getFlux = template.opsForValue()
                .get(key)


        StepVerifier
                .create(Flux
                        .from(setFlux)
                        .thenMany(getFlux)
                )
                .expectSubscription()
                .assertNext(this::carAssertions)
                .expectComplete()
                .verify()
    }

    fun carAssertions(car: Car) {
        assertAll("Car has State",
                { Assertions.assertNotNull(car) },
                { Assertions.assertEquals("red", car.color) })
    }
}