package com.example.phi.demo

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
class Note(@Id val uuid: UUID, val text: String)
