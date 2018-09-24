+++
date = 2018-09-13
publishDate = 2018-09-19
title = "Testing Reactive Apps with SpringBoot - The Consumer"
description = "Use Spring-boot 2.x to verify test stages of your WebFlux and Reactive Data Apps"
toc = true
categories = ["reactive", "webflux", "spring", "test", "bootifultest", "cdct", "cdc", "test-frameworks"]
tags = ["reactive", "web", "webflux", "test", "java", "spring", "demo", "consumer", "cdct"]
+++

# The Producer/Consumer Duality (their exchange)

In the producer side, we setup a service that will let us query a database of teams. This article will focus on the consumer side of the communication chain - namely how to extract tests out of situations where the comunication chain is asymetrical. We will then dive into Spring Cloud Contract to aleviate this issue and produce a working producer/consumer contract.

To start, we'll need a data object for client state. The following `Team` data class describes the shape of our objects.

Team.java:

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Team {
    private String id;
    private String name;
}
```

Looks similar to our Producer variant, except this data will not get persisted as it's client bounded. What we're interested next is ensuring we can get this object consistently out of our client. This means possibly mocking a request/response exchange, and maybe even talking to a real server for test.

To do that, we have a couple choices: first we will mock the request/response uniformly across our client and service. The only thing we will need to produce is a guestimate of how our service should work. Lets look into this method now. There is a framework that you may know of called [WireMock](http://www.wiremock.org). We can use it to stub out our service and response conditions.

## Consumer / Client implementation

This example needs a client that interacts with our proucer in some meaningful way. Lets use the `@Component` stereotype to create a resource which HTTP/GET's our producers service endpoint and performs it's intended business function - return a list of Team's.

```java
@Component
public class SportsNetClient {
    @Value("${server.url:http://localhost:8080/teams/all}")
    String url;

    private final WebClient webClient;

    public SportsNetClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<Team> getAllTeams() {
        return this.webClient
                .get()
                .uri(url)
                .retrieve()
                .bodyToFlux(Team.class);
    }
}
```

This component will obviously need a [WebClient]() to interact in HTTP. We can build one in our `main` configuration class.

```java
@SpringBootApplication
public class SportsNetClientApp {

    @Bean
    WebClient client() {
        return WebClient.builder().build();
    }

    public static void main(String[] args) {
        SpringApplication.run(SportsNetClientApp.class, args);
    }
}
```

Now all we need to do is test the interaction.  Will our client successfully request and respond with our producer service? How can we tease out a test for service we have not actually engaged nor received documented behaviour for? Lets answer these question in the next section!

# Mocked Wire exchanges - WireMock

As stated, we will begin exploring what stubbed HTTP service code looks like in a spring boot application. To begin using Wiremock and to avoid confusion, we should disable any configured web servers - set `webEnvironement` to `NONE` -  Since our wiremock server will consume it's own service resources. 

Next, to load the client resources we put together earlier, `@Import` the resource that exposes our client-behaving infrastructure.

Finally, we can enable WireMock such that it listens to any TCP port of our choosing. Additionally, we want marshalling/de-marshalling behaviour. Adding the [@AutoConfigureJsonTesters]() gives us an [ObjectMapper]() to play with. With it, you will also get access to AssertJ based JSON tester API's that can be used with Jackson, and Jsonb including a generic, [BasicJsonTester]().

Configuration for WireMockTest.java:

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
@Import({SportsNetClientApp.class, SportsNetClient.class})
@org.springframework.cloud.contract.wiremock.AutoConfigureWireMock(port = 8080)
public class SportsNetClientWireMockTests {

    @Autowired
    private SportsNetClient client;

    @Autowired
    ObjectMapper    objectMapper;

    private final Team first = new Team("1", "REDS");
    private final Team second = new Team("2", "BLUES");
//...
```

Define our stubbed service behaviour using the WireMock Fluent API. With knowledge of our service behaviour, we can create an approximation of what to expect from the production variety. We can start this in a pre-amble with the `@Before` annotation to compose the request/response behaviour before any tests get run.

This mock will respond to requests on path "/teams/all" with an array of 2 teams.

```java
    @Before
    public void setupWireMock() throws JsonProcessingException {
        String jsonBody = objectMapper.writeValueAsString(Arrays.asList(first, second));

        WireMock.stubFor(
                WireMock
                        .get("/teams/all")
                        .willReturn(
                                WireMock
                                        .aResponse()
                                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
                                        .withBody(jsonBody)
                        )
        );

    }
//...
```

With our service behaviour stubbed, lets compose the client flow. Simply use our production client as-is and validate the response. Since this is a reactive test, we can use [StepVerifier]() to do the work of verifiying our results.

```java
    @Test
    public void testShouldFetchTeams() {
        Flux<Team> customers = this.client.getAllTeams();
        StepVerifier
                .create(customers)
                .expectNext(new Team("1", "REDS"))
                .expectNext(new Team("2", "BLUES"))
                .verifyComplete();
    }

}
```

## Executing a WireMock Scenario

When executing this scenario, be sure to have all client facing service URL's lined up with the WireMock server configuration. For example our `ServerURL` setup in the `SportsNetclient` class. Lets take a look at output and figure out what we learned.

```sh
$ mvn test
... logging output ...
2018-09-24 18:45:10.091  INFO 73348 --- [qtp629016615-31] WireMock                                 : Received request to /mappings with body {
  "id" : "278674c3-3953-41a2-ad11-58cb02ec6d8f",
  "request" : {
    "url" : "/teams/all",
    "method" : "GET"
  },
  "response" : {
    "status" : 200,
    "body" : "[{\"id\":\"1\",\"name\":\"REDS\"},{\"id\":\"2\",\"name\":\"BLUES\"}]",
    "headers" : {
      "Content-Type" : "application/json;charset=UTF-8"
    }
  },
  "uuid" : "278674c3-3953-41a2-ad11-58cb02ec6d8f"
}
2018-09-24 18:45:10.670  INFO 73348 --- [qtp629016615-32] /                                        : RequestHandlerClass from context returned com.github.tomakehurst.wiremock.http.StubRequestHandler. Normalized mapped under returned 'null'
2018-09-24 18:45:10.679  INFO 73348 --- [qtp629016615-32] WireMock                                 : Request received:
127.0.0.1 - GET /teams/all

User-Agent: [ReactorNetty/0.7.8.RELEASE]
Host: [localhost:8089]
Accept-Encoding: [gzip]
Accept: [*/*]



Matched response definition:
{
  "status" : 200,
  "body" : "[{\"id\":\"1\",\"name\":\"REDS\"},{\"id\":\"2\",\"name\":\"BLUES\"}]",
  "headers" : {
    "Content-Type" : "application/json;charset=UTF-8"
  }
}

Response:
HTTP/1.1 200
Content-Type: [application/json;charset=UTF-8]


[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 7.732 s - in com.example.sportsnet.SportsNetClientWireMockTests

```

The majority of the logging we comment out, and focus on WireMock here. Since it ouputs the stub, we'll get a JSON representation for the stub we'll talk to. Then we get the actual request/response communictation.

Because we expect this to run successfully, we'll see just the Success output for this test. On the other hand, we can expect a terse output when our contracts dont go right. lets call the test for "/teams/favorites" which for some reason expects to send a slightly different output.

```sh

```

Running `mvn clean install` with a passing verification will also publish an artifact to archive or local dependency repository. We can now wrap up the Producer side of our tests, and begin focusing on the [Consumer side](https://www.sudoinit5.com/post/spring-boot-testing-consumer/).

# Knowledge for This example

* [WebTestClient Documentation](https://docs.spring.io/spring/docs/current/spring-framework-reference/pdf/testing-webtestclient.pdf)

* [Spring Cloud Contract DSL](https://cloud.spring.io/spring-cloud-contract/multi/multi__contract_dsl.html)

* [Fowler on CDC](https://martinfowler.com/articles/consumerDrivenContracts.html)

* [Wiremock Proxying](http://wiremock.org/docs/proxying/) Also describes programattic, cmd-driven approach to record/playback.