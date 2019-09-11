package com.example.streamy

import org.springframework.data.redis.connection.stream.MapRecord
import org.springframework.data.redis.connection.stream.RecordId
import org.springframework.data.redis.connection.stream.StreamOffset
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Service
class MessageService(val template: ReactiveStringRedisTemplate) {
    fun get(streamKey: String): Flux<Message> = template
            .opsForStream<String, String>()
            .read(StreamOffset.fromStart(streamKey))
            .map {
                Message(UUID(it.id.timestamp!!, it.id.sequence!!), it.value["from"]!!, it.value["text"]!!)
            }
            .checkpoint("receive")

    fun put(streamKey: String, from: String, text: String): Mono<UUID> {
        val map = mapOf(
                Pair("from", from),
                Pair("text", text))

        return template
                .opsForStream<String, String>()
                .add(MapRecord
                        .create(streamKey, map)
                        .withId(RecordId.autoGenerate()))
                .map {
                    UUID(it.timestamp!!, it.sequence!!)
                }
                .checkpoint("send")
    }
}