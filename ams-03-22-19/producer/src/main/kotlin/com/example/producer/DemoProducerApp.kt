package com.example.producer

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.RedisSerializationContext.RedisSerializationContextBuilder
import org.springframework.data.redis.serializer.StringRedisSerializer

@SpringBootApplication(exclude = [RedisAutoConfiguration::class, RedisRepositoriesAutoConfiguration::class])
class DemoProducerApp

fun main(args: Array<String>) {
    runApplication<DemoProducerApp>(*args)
}


@Configuration
class RedisConfiguration {

    @Bean
    fun redisConectionFactory() = LettuceConnectionFactory()

    @Bean
    fun someCacheBuilder(): RedisSerializationContextBuilder<String, Car> {
        val keys = StringRedisSerializer()
        val values = Jackson2JsonRedisSerializer(Car::class.java)
        values.setObjectMapper(jacksonObjectMapper())           // KOTLIN USERS : use setObjectMapper!

        val builder: RedisSerializationContextBuilder<String, Car> =
                RedisSerializationContext.newSerializationContext(keys)

        builder.key(keys)
        builder.value(values)
        builder.hashKey(keys)
        builder.hashValue(values)

        return builder
    }

    @Bean
    fun someCache(cf: ReactiveRedisConnectionFactory): ReactiveRedisTemplate<String, Car> =
            ReactiveRedisTemplate(cf, someCacheBuilder().build())


}