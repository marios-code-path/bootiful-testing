package com.example.parisproducer

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import java.util.*

interface AccordionRepository : ReactiveMongoRepository<Accordion, UUID>