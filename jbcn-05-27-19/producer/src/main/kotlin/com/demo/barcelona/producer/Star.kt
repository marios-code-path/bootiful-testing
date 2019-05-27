package com.demo.barcelona.producer

import org.springframework.data.cassandra.core.mapping.Column
import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.Table
import java.util.*

@Table("stars")
class Star(
        @Column("id")
        val id: UUID,
        @PrimaryKey("name")
        val name: String,
        @Column("magnitude")
        val magnitude: Double)