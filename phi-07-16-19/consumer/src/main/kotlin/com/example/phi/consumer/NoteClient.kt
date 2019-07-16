package com.example.phi.consumer

import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux

@Component
class NoteClient(val webClient: WebClient) {

    fun getAll(): Flux<Note> = webClient
            .get()
            .uri("http://localhost:8090/all")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .exchange()
            .flatMapMany {
                it.bodyToFlux(Note::class.java)
            }
}
