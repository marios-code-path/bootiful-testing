package com.example.producer;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@DataMongoTest
public class DemoReactivePersistenceTest {

    @Autowired
    UserRepository repo;

    Matcher<User> userMatcher = Matchers.allOf(
            Matchers.notNullValue(),
            Matchers.hasProperty("id", Matchers.notNullValue()),
            Matchers.hasProperty("name", Matchers.equalTo("John"))
    );

    @Test
    public void testShouldSaveFind() {
        User user = new User(null, "John");

        Publisher<User> userSave = repo.save(user);

        Publisher<User> userQuery = repo.findAll();

        Publisher<User> compose = Flux
                .from(userSave)
                .thenMany(userQuery);

        StepVerifier
                .create(compose)
                .expectSubscription()
                .assertNext( u -> MatcherAssert.assertThat("User State", u, userMatcher) )
                .verifyComplete();
    }

}
