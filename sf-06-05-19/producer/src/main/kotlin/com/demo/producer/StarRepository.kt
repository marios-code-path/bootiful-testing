package com.demo.producer

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository

interface StarRepository : ReactiveCassandraRepository<Star, String>