package com.example.producer;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TicketRepository extends ReactiveMongoRepository<Ticket, String> {
}
