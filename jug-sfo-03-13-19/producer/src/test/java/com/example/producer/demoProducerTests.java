package com.example.producer;

import org.assertj.core.api.Assertions;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
public class demoProducerTests {

    @Test
    public void testUserShouldHoldState() {
        User user = new User("1234", "John");

        Assertions.assertThat(user)
                .as("Has full state, no nulls")
                .isNotNull()
                .hasNoNullFieldsOrProperties();
    }

    Matcher<User> userMatcher = Matchers.allOf(
            Matchers.notNullValue(),
            Matchers.hasProperty("name",Matchers.equalTo("John"))
    );

    @Test
    public void testUserShouldStepVerify() {
        Mono<User> userMono = Mono.just(new User("1234", "John"));

        StepVerifier
                .create(userMono)
                .expectSubscription()
                .assertNext( u -> MatcherAssert.assertThat("User State", u, userMatcher) )
                .expectComplete()
                .verify();
    }

}
