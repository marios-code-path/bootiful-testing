package com.example.phi.demo

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import java.util.*

interface NoteRepository : ReactiveMongoRepository<Note, UUID>
