package com.demo.istanbul.producer

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import java.util.*

interface DemoUserRepo : ReactiveMongoRepository<DemoUser, UUID>