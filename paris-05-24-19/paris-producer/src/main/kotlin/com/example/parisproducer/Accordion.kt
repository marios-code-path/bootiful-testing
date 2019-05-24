package com.example.parisproducer

import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
data class Accordion(val id: UUID, val name: String)