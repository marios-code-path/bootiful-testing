package com.woburn.producer

import org.springframework.data.mongodb.core.mapping.Document
import java.util.*


@Document
data class Tax(val id: UUID, val rate: String)