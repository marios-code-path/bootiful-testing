package com.sportsnet;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

interface TeamRepository extends ReactiveMongoRepository<Team, String> {

    @Query("{name: {$in: ['REDS', 'BLUES']} }")
    Flux<Team> getMyFavorites();

    Mono<Team> findByName(String name);
}
