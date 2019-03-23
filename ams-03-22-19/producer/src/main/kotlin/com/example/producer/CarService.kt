package com.example.producer

import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.*

@Service
class CarService(val template: ReactiveRedisTemplate<String, Car>) {

    fun newCar(car: Car): Mono<String> {
        val key = UUID.randomUUID().toString()

        return template.opsForValue()
                .set(key, car)
                .map {
                    key
                }
    }

    fun getCar(key: String): Mono<Car> {
        return template.opsForValue()
                .get(key)
    }
}
