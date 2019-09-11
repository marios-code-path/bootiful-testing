package com.woburn.producer

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import java.util.*

interface TaxRepository : ReactiveMongoRepository<Tax, UUID>