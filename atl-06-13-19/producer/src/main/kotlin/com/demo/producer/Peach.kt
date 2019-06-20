package com.demo.producer

import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
data class Peach(val id: UUID, val size: String)