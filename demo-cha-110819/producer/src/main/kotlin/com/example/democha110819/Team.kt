package com.example.democha110819

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import java.util.*

data class Team(val id: UUID, val name: String)

interface TeamRepository : ReactiveMongoRepository<Team, UUID>