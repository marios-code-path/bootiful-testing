package com.sportsnet;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

interface TeamRepository extends ReactiveMongoRepository<Team, String> {

    Flux<Team> findByName(String name);
}
