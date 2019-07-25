package com.woburn.producer

import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
data class  Cereal(val id: UUID, val name: String)