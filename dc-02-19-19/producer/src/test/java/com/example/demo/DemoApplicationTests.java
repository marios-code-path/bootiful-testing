package com.example.demo;


import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;


@DataMongoTest
public class DemoApplicationTests {

    @Autowired
    PersonRepository repo;

    Matcher<Person> personMatcher = Matchers.allOf(Matchers.hasProperty("name", Matchers.equalTo("Bob")),
            Matchers.hasProperty("id"), Matchers.notNullValue()
    );

    @Test
    public void testShouldAssertEntity() {
        Person p = new Person("1234", "Bob");

        Assertions.assertNotNull(p);
        Assertions.assertNotSame(p, new Person("1234", "Bob"));

        MatcherAssert.assertThat("Person has normal state",
                p,
                personMatcher);
    }

    @Test
    public void testShouldSaveFind() {
        Person p = new Person(null, "Bob");

        Publisher<Person> save = repo.save(p);
        Publisher<Person> find = repo.findAll();
        Publisher<Person> composition = Flux.from(save)
                                        .thenMany(find);

        StepVerifier
                .create(composition)
                .expectSubscription()
                .assertNext(it -> MatcherAssert.assertThat("state", it, personMatcher))
                .expectComplete()
                .verify();
    }


}
