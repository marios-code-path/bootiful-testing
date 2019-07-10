package com.example.producer

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
data class Message(@Id val id: UUID, val text: String)