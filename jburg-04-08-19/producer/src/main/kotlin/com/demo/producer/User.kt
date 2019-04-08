package com.demo.producer

import java.time.Instant
import java.util.*

data class User(val id: UUID, val name: String, val modified: Instant)
