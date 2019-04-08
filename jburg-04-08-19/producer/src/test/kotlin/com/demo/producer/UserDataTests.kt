package com.demo.producer

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@DataMongoTest
@ExtendWith(SpringExtension::class)
class UserDataTests {

    @Test
    fun `should publish user`() {

    }

}