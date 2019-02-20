package com.example.demo;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ExtendWith(SpringExtension.class)
@AutoConfigureStubRunner(
    ids = "com.example:demo:+:8090",
    stubsMode = StubRunnerProperties.StubsMode.LOCAL)
public class DemoApplicationTests {

  @Autowired DemoClient client;

  Matcher<Person> personMatcher =
      Matchers.allOf(
          Matchers.hasProperty("name", Matchers.equalTo("Bob")),
          Matchers.hasProperty("id"),
          Matchers.notNullValue());

  @Test
  public void shouldClientSeeAll() {
    StepVerifier.create(client.getAll())
        .expectSubscription()
        .assertNext(it -> MatcherAssert.assertThat("Person is Bob", it, personMatcher))
        .verifyComplete();
  }
}
