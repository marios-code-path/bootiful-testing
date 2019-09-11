package com.example.streamy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StreamyApplication

fun main(args: Array<String>) {
    runApplication<StreamyApplication>(*args)
}

