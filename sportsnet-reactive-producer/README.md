+++
date = 2018-09-13
publishDate = 2018-09-19
title = "Testing Reactive Apps with SpringBoot"
description = "Use Spring-boot 2.x to verify test stages of your WebFlux and Reactive Data Apps"
toc = true
categories = ["reactive", "webflux", "spring", "test", "bootifultest", "cdct", "cdc", "test-frameworks"]
tags = ["reactive", "web", "webflux", "test", "java", "spring", "demo", "producer", "cdct"]
+++

# The Producer Environment

In the world of testing critical business funcationality, we dont need much inspiration to get the job  done. However, when it comes to *what* to use, you may be left wandering whether you'll hit all of the right frameworks and tools to validate your business code. Also likely, you'll also need to figure out how to cross validate a producer app with a consumer app(s) that you may not even own!

This article will go into detail to show tools and techniques you'll likely need during your journey to satisfactory test coverage. Well written tests and coverage, means giving other developers the opportunity to make good decisions about what happens in later iterations.

Lets start with a simple app: we want to store and query sports teams into MongoDB. Furthermore, this needs to be exposed with RESTful API. So specifically we want to leverage JSON object mapping, HTTP services, Data Repositories, etc.. We'll tackle the producer side here which is fancy for `"The server side"` usually.

The source to the example lives [here](https://github.com/marios-code-path/bootiful-testing/tree/master/sportsnet-reactive-producer).

## The Data Domain

To start, we'll need a model or domain., The following Team data class describes our data succinctly.

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

Its not a lot, but this should let us get started with the application and techniques at hand. What we're interested next is testing data, and whether it can handle being composed or altered with consistency. We will need to answer questions such as: Will our data get saved as it should (i.e not consistent state), and will our repository find the right collection items? How should one verify that? Lets take a look at a reactive testing componnet that makes doing this expectation-oriented work easier.

## Imperative Test Assertions

The following two tests take care of the question whether our data classes can be properly handled without side-effects. To do this, we can test both cases - imperative access, and reactive access.

First, in imperative tests we will use [AssertJ](http://joel-costigliola.github.io/assertj/) assertions to make sure our data has the correct state. AssertJ can be activated in your projects by adding `spring-boot-starter-test` to your dependency list. Using it's Fluid API lets us make concise state expectations.

```java
public class TeamTest {

    private final Team myTeam = new Team("1903", "Beşiktaş");

    @Test
    public void testShouldBeConsistent() throws Exception {

        Assertions.assertThat(myTeam.getId()).isEqualToIgnoringWhitespace("1903");
        Assertions.assertThat(myTeam.getName()).isEqualToIgnoringWhitespace("Beşiktaş");
    }
//...
```

## The Reactive StepVerifier

Then we can verify that our objects are accessible through reactive methods. And this is where we take extra steps in describing the reactive [StepVerifier](https://projectreactor.io/docs/test/release/api/reactor/test/StepVerifier.html). The `StepVerifier` will help us understand how our
data gets processed in each step of an reactive stream. There are a couple options to creating the first step of the verification process which setup the behaviour of the underlaying scheduler. In this example, we use the standard `.create(Flux[N])` method to push our one data element
into a stream with expectations that our one element is observed before a completion signal gets sent to downstream subscribers.

It should be noted that calling of `verify()`, `verifyThenAssertThat()`, `verify(Duration)` triggers the verification of all expectations above it. Additionally we put `expectComplete()` above verify to denote that we expect the subscription to be closed. Note that we could easily replace these 2 functions with `verifyComplete()` and is trivial to change, but this is good to know in case you have additional concerns in mind between stream completion and verification.

```java
//...
    @Test
    public void testShouldStepVerifyTeams() throws Exception {
        StepVerifier.create(Flux.just(myTeam.getId(), myTeam.getName()))
                .expectNext("1903", "Beşiktaş")
                .expectComplete()
                .verify();
    }
} // END TeamTest
```

# Adding a Persistence Layer (Prod)

Lets move from ordinary state and imperative knowledge, to the world of data persistence. Our repository will be constructed from the reactive MongoDB variety. We can interact with our data by specifying the data and ID types to the [RectiveMongoRepository](https://docs.spring.io/spring-data/mongodb/docs/current/api/org/springframework/data/mongodb/repository/ReactiveMongoRepository.html) interface. This interface also lets us compose mongodb queries to fetch and extract our data.

```java
interface TeamRepository extends ReactiveMongoRepository<Team, String> {
    @Query("{$or: [{name: \\/REDS|BLUES\\/}]}")
    Flux<Team> getMyFavorites();

    Mono<Team> getTeamByName(String name);
}
```

In addition to the above mentioned query, we also get the complete host of Spring Data Repository operations.
adding our `getMyFavorites()` query is contributing variance which we will need to test. In contrast, our `getTeamByName` method produes a query that is based on our actual model property, thus any changes to the method or propery name 'name' could render the method broken.

## Sliced Framework Tests

Make your data tests run quickly by slicing up the Spring Context at startup time. This can be done using [Spring Test Slices](https://spring.io/blog/2016/08/30/custom-test-slice-with-spring-boot-1-4) and extending your slice (framework test dependency exclusions were introduced in Spring Boot 1.4) to meet your needs during that phase of the test. We can objserve this with [DataMongoTest](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/test/autoconfigure/data/mongo/DataMongoTest.html) which provided just the mongodb testing supports used in the next few units. It requires a very simple test context bootstrap, followed by specific exclusions that are selective eliminations of our full path-scaning behaviour. Finally, it adds just `spring-mongo` framework resources to component scan.

We can explore this `DataMongoTest` annotation further, by examining it's declaration.

DataMongotest.java:

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@BootstrapWith(SpringBootTestContextBootstrapper.class)
@OverrideAutoConfiguration(enabled = false)
@TypeExcludeFilters(DataMongoTypeExcludeFilter.class)
@AutoConfigureCache
@AutoConfigureDataMongo
@ImportAutoConfiguration
public @interface DataMongoTest {
    //...
```

This test slice is a composition of: a standard bootstrap context (the `@BootstrapWith(SpringBootTestContextBootstrapper.class)`), an [TypeExclusionFilter](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/context/TypeExcludeFilter.html) to inhibit autoscanning of any non Data/Mongo configurations. And Finally several "slices" (like modules in Guice) - autoconfigurations for Cache, and MongoDB.

The use of the provided [Auto Configured Tests](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html#boot-features-testing-spring-boot-applications-testing-autoconfigured-tests) helps specify specific feature "slices" we want to re-use. Thusly the `@DataMongoTest` is limited to just Cache ( in which NONE is default ), and Mongo-related data ( E.G. `@Document`, `DataMongoRepository` ) resources.

    NOTE: Test slices like `@DataMongoTest` can be tweaked/extended, or you can work your own conveinient slice for the resources that make most sense to your application.

The end result is always that our test context will contain only components needed to execute a certain test or test suite. This is like enforcing separation of concerns ( dont test data when testing web) and allows developers to concentrate on the layer of our app in which test test must validate, while also speeding up execution time.

## Embedding MongoDB During Test

Lets look at the what our persistence tests will look like. The `@DataMongoTest` class shows us we can expect any mongo based framework components and resources - network resources - to get configured. An embedded ( ephemeral, forked process ) instance can be setup with the [Embedmongo.flapdoodle.de](https://flapdoodle-oss.github.io/de.flapdoodle.embed.mongo/) plugin, if any are on the classpath. Otherwise, expect to stand up a single mongodb instance for local connections to test with.

Installing Mongodb locallay with Homebrew:

```sh
$ brew install mongodb
$ brew services start mongodb
==> Successfully started `mongodb` (label: homebrew.mxcl.mongodb)
$ # ahhh
```

Lets consider the following maven build (pom.xml) dependency needed to get the `Embedmongo` plugin working:

```xml
<dependency>
 <groupId>de.flapdoodle.embed</groupId>
 <artifactId>de.flapdoodle.embed.mongo</artifactId>
 <scope>test</scope>
</dependency>
```

Now, we can expect the embedded mongo instance to stand in place of our locally deployed mongo instance.

## Our tests

This test will determine that our repository query executes as intended. We can tease out the next test by
issuing commands for populating our mongo collection, then finding the data using our custom repository method. Finally, StepVerifier comes in handy to perform the assertion work of our unit.

```java
@DataMongoTest
@RunWith(SpringRunner.class)
public class TeamRepositoryTest {

    @Autowired
    private TeamRepository repo;

    private final Team one = new Team("1", "BLUES");
    private final Team two = new Team("2", "REDS");
    private final Team falsity = new Team("3", "REDS");

    @Test
    public void testShouldFetchFavorites() {
        Publisher<Team> setup =
                this.repo
                        .deleteAll()
                        .thenMany(this.repo.saveAll(Flux.just(this.one, this.two)));

        Publisher<Team> find = this.repo.getMyFavorites();

        Publisher<Team> composite = Flux
                .from(setup)
                .thenMany(find);

        StepVerifier
                .create(composite)
                .expectNext(this.one, this.two)
                .verifyComplete();
    }
}
```

This `testShouldFetchFavorites` unit simply interacts with mongo to store state, and retrieve it. We are able to compose a relatively simple reactive stream, and verify that our data made it into mongo, and back without side-effects.

The [RunWith(SpringRunner.class)](http://junit.sourceforge.net/javadoc/org/junit/runner/RunWith.html) annotation tells JUnit to start spring-dependency injection and auto-scanning for our test. It is a convienence of manually configuring the [SpringJUnit4ClassRunner](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/test/context/junit4/SpringJUnit4ClassRunner.html) done in prior versions of Spring.

This `@DataMongoTest` will get executed against the in-memory mongo instance we configured earlier. Thus no actual state is kept. On the other hand, if you're using real instances, you'll need to ensure that this data gets deleted each time.

# The WebFluxTest

Now to the RESTful HTTP services. We want to ultimately expose our data to web clients. We can acheive this in the reactive world with [WebFlux](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html). Lets put together a couple of endpoints to access our app in test (and prod).

```java
@Configuration
public class SportsNetWebConfig {

    @Bean
    RouterFunction<ServerResponse> routes(TeamRepository cr) {
        return
                route(GET("/teams/all"), r -> ServerResponse.ok().body(cr.findAll(), Team.class))
                .and(route(GET("/teams/favorite"), r -> ServerResponse.ok().body(cr.getMyFavorites(), Team.class)));

    }
}
```

In this service, we use programmatic functional interfaces to declare our HTTP endpoints. Here we simply route `"teams/all"` to an OK response with a body continaing the JSON output of our `findAll()` repository method. Similarly, the `"/teams/favorite"` will route to the JSON output of our `getMyFavorites()` repository method.

## The WebFlux Test Slice

Similar to `@DataMongoTest` slice in which the data stack is sliced out and exposed exclusively, we have a test slice that exhibits similar behaviour for the Web stack. Lets look into this annotation to see what it gives us.

Using the [@WebFluxTest](https://docs.spring.io/autorepo/docs/spring-boot/current/api/org/springframework/boot/test/autoconfigure/web/reactive/WebFluxTest.html) meta annotation gives us our WebFlux HTTP service capability. We can add JSON support (mapping and testing) through a related "slice" called [@AutoConfigureJsonTesters](). Lets look further into the `@WebFluxTest` code.

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@BootstrapWith(WebFluxTestContextBootstrapper.class)
@OverrideAutoConfiguration(enabled = false)
@TypeExcludeFilters(WebFluxTypeExcludeFilter.class)
@AutoConfigureCache
@AutoConfigureWebFlux
@AutoConfigureWebTestClient
@ImportAutoConfiguration
public @interface WebFluxTest {
```

Without going into too much detail, we can describe this class as a slice which gives us our bootstrapped `WebFluxTestContextBootstrapper` context, in addition to autoscanned WebFlux-friendly components ( @Controller's , @Converter's, WebfluxConfigurer ) through it's `ExclusionFilter` [WebFluxTypeExcludeFilter]() we get test WebFlux support through [@AutoConfigureWebFlux](https://docs.spring.io/autorepo/docs/spring-boot/current/api/org/springframework/boot/test/autoconfigure/web/reactive/AutoConfigureWebFlux.html) and the [@AutoConfigureWebTestClient](https://docs.spring.io/autorepo/docs/spring-boot/current/api/org/springframework/boot/test/autoconfigure/web/reactive/AutoConfigureWebTestClient.html) provides a configured [WebTestClient](https://docs.spring.io/spring-framework/docs/5.0.2.BUILD-SNAPSHOT/javadoc-api/org/springframework/test/web/reactive/server/WebTestClient.html) which if used as is, connects to a standard running 'LIVE' server.

## Our WebFlux Test

```java
@WebFluxTest
@Import(SportsNetWebConfig.class)
@RunWith(SpringRunner.class)
@AutoConfigureJsonTesters
public class SportsNetWebTest {

    @Autowired
    private ObjectMapper    objectMapper;

    @Autowired
    private SportsNetWebConfig webConfig;

    @MockBean
    private TeamRepository repository;

    Team red = new Team("1", "REDZS");
    Team blue = new Team("2", "BLUES");

    @Before
    public void before() {

        Mockito
                .when(this.repository.findAll())
                .thenReturn(Flux.just(red, blue));

        Mockito
                .when(this.repository.getMyFavorites())
                .thenReturn(Flux.just(blue));

        Mockito
                .when(this.repository.findByName(Mockito.anyString()))
                .thenReturn(Mono.just(blue));
    }


// Test... to follow
}
```

Due to exluion filters - we didnt add any test data layer slice - there is no backing data layer for real per-se, a lack of persistence. To aleiviate this we can use [Mockito](https://site.mockito.org/) to mock and stub our data repositories. Here, we'll mock our team `TeamRepository` beans using the [@MockBean](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/test/mock/mockito/MockBean.html) annotation. Then setup our mock stubs inside a `@before` clause or even on top of the unit itself. This setup simply interacts with `Mockito` directly and lets us define the behavior of our repository.

## The WebTestClient

In this example, we want to test our HTTP controller (routes) but not connect to a live server. The `WebTestClient` allows mocked `exchange` request/respondses by exposing multiple [bindToXxx](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/test/web/reactive/server/WebTestClient.html#bindToApplicationContext-org.springframework.context.ApplicationContext-) methods. This lets us bind our client to specific points in our web application for example ; [Controllers](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/test/web/reactive/server/WebTestClient.html#bindToController-java.lang.Object...-), [RouterFunctions](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/test/web/reactive/server/WebTestClient.html#bindToRouterFunction-org.springframework.web.reactive.function.server.RouterFunction-), etc... Our Sample will bind to the RouterFunction we defined in our REST service earlier. For more information on `WebTestClient` binding options, see  the [Spring Framework Reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/pdf/testing.pdf#mock-objects-web-reactive):

    The package `org.springframework.mock.http.server.reactive` contains mock implementations of
    `ServerHttpRequest` and `ServerHttpResponse` for use in WebFlux applications. The package
    `org.springframework.mock.web.server` contains a mock `ServerWebExchange` that depends on those
    mock request and response objects.

By exposing the underpinnnings of our service application, we get to talk to our RESTful endpoints without spinning up any additional resources, while in the process making our tests faster to execute, this adds to make our tests less complicated.

```java

    @Test
    public void testShouldBetByName() throws JsonProcessingException {

        String jsonBlob = objectMapper.writeValueAsString(blue);

        WebTestClient
                .bindToRouterFunction(webConfig.routes(repository))
                .build()
                .get().uri("/teams/byName?name=BLUES")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .json(jsonBlob);
    }
    
    @Test
    public void testShouldGetAll() {

        WebTestClient
                .bindToRouterFunction(webConfig.routes(repository))
                .build()
                .get().uri("/teams/all")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.[0].id").isEqualTo("1")
                .jsonPath("$.[0].name").isEqualTo("REDS")
                .jsonPath("$.[1].id").isEqualTo("2")
                .jsonPath("$.[1].name").isEqualTo("BLUES");
    }
```

Our client is ready after calling `build()`. We can then specify the request/response expections that will get invocated to our `RouterFunction`. Calling `Exchange` with the `WebTestClient`, will allow us to assemble the assertion path. This [WebTestClient.ResponseSpec](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/test/web/reactive/server/WebTestClient.ResponseSpec.html) provides a fluent API that we then use to plug in our expectations.  Specially worth mentioning is th use of JSON-specific expressions using via `jsonPath` queries to identify body characteristics. This feature stems from the re-use of [com.jayway](https://github.com/json-path/JsonPath) [JSONPath](http://goessner.net/articles/JsonPath/) evaluator.

## Testing Web with StepVerifier

When you expect to have standard `Flux<type>` rules over your test cases, then use `returnResult(class)` to express test expectations with `StepVerifier`.

```java

    @Test
    public void testShouldGetFavs() {

        StepVerifier.create(WebTestClient
                .bindToRouterFunction(webConfig.routes(repository))
                .build()
                .get().uri("/teams/favorites")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .returnResult(Team.class)
                .getResponseBody()
        )
                .expectSubscription()
                .expectNext(new Team("1", "REDS"))
                .expectNext(new Team("2", "BLUES"))
                .verifyComplete();

    }
```

We lose the ability to introspect HTTP conditions, but we gain the ability to use a common reactive test
approach to our service.

# The need for Contracts

While verification of our web endpoints on the service is alone neccessary and substantially beneficial to the code base. It's no better visibile to the client as to how this service behaves. What does the client developer expect to do in place? It's usually something like deploy the trunk or tag of this service and test against it. Which means deploying additional resources like databases, and before we know it, theres several servers running to support this ephemeral test.
<!-- Insert a pic -->

Consumer Driven Contract Testing means we can share contracts (of service behavior) between both sides of the communication chain. This example will verify the `producer` side of this HTTP request / response exchange.
<!-- Insert a pic -->

[Spring Cloud Contract](https://cloud.spring.io/spring-cloud-contract/) allows us to engage this method of development with ease. The next couple of sections explores this.

## Spring Cloud Contract Maven Plugin

To get started, we will implement the build plugin that tells our build tool (Maven) to assemble a Verification test to our contract that we will write later. This test will extend the class we create in the `baseClassForTests` config element. Ensure we turn our [TestMode](https://cloud.spring.io/spring-cloud-contract/multi/multi__spring_cloud_contract_verifier_setup.html#gradle-configuration-options) to `Explicit` so verification happens through live socket HTTP request/response.

```xml
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
            <plugin>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-contract-maven-plugin</artifactId>
                <version>${spring-cloud-contract.version}</version>
                <extensions>true</extensions>
                <configuration>
                    <baseClassForTests>
                        com.sportsnet.BaseClass
                    </baseClassForTests>
                    <testMode>EXPLICIT</testMode>
                </configuration>
            </plugin>
        </plugins>
    </build>
```

This base class will have to allow access to a running or mock HTTP service. The easiest way to do this is to use [RestAssured](http://rest-assured.io/) API to expose your routeFunctions, since it's used as a default client/server. Alternately you can set `"spring.main.web-application-type=reactive"` in properties to enable the Reactive WebFlux servers. The clients may still use restassured, unless it's been disabled through dependency exlusion in your build tool, however.

Here is our verificaion test BaseClass. It will setup a RestAssured MVC server containing our RouterFunction definition, while mocking out our repository for data queries.

```java
@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"server.port=0"}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseClass {

    @LocalServerPort
    private int port;

    @Configuration
    @Import(SportsNetWebConfig.class)
    public static class TestConfiguration {
    }

    @MockBean
    private TeamRepository repository;

    @Before
    public void before() throws Exception {

        RestAssured.baseURI = "http://localhost:" + this.port;

        Mockito
                .when(this.repository.findAll())
                .thenReturn(Flux.just(new Team("1", "REDS"), new Team("2", "BLUES")));

    }
}
```

## The contract definition

Now that we have enabled a plugin, and a baseclass to derive our contract verification test. We can now define the actual contract.

This contract makes use of the GroovyDSL (with YAML being an option) to define the request and response this service will emit. This assumes we will request all teams from the `"/teams/all"` endpoint, and expect our 2 pre-programmed Teams (see BaseClass above). If you have multiple scenarios for any service, then we may write a separate contract that defines it's specific behaviour ( e.g. No Teams found ).

```groovy
import org.springframework.cloud.contract.spec.Contract
import org.springframework.cloud.contract.spec.internal.HttpMethods

Contract.make {
    request {
        method HttpMethods.HttpMethod.GET
        url "/teams/all"
    }
    response {
        body(
        """
            [
            { "id": 1, "name" : "REDS" },
            { "id": 2, "name" : "BLUES" }
            ]
        """
        )
        status(200)
        headers {
            contentType(applicationJsonUtf8())
        }
    }
}
```

Now, execute `mvn clean test` and our plugin will generate and execute a (documented) verification test that looks similar to the following:

```java
public class ContractVerifierTest extends BaseClass {

@Test
public void validate_shouldReturnAllTeams() throws Exception {
// given:
        RequestSpecification request = given();

// when:
        Response response = given().spec(request)
            .get("/teams/all");

// then:
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.header("Content-Type")).matches("application/json;charset=UTF-8.*");
// and:
        DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
        assertThatJson(parsedJson).array().contains("['name']").isEqualTo("REDS");
        assertThatJson(parsedJson).array().contains("['id']").isEqualTo(1);
        assertThatJson(parsedJson).array().contains("['name']").isEqualTo("BLUES");
        assertThatJson(parsedJson).array().contains("['id']").isEqualTo(2);
    }

}
```

Running `mvn clean install` with a passing verification will also publish an artifact to archive or local dependency repository. We can now wrap up the Producer side of our tests, and begin focusing on the [Consumer side](https://www.sudoinit5.com/post/spring-boot-testing-consumer/).

# Knowledge for This example

* [Source Code to this Demo](https://github.com/marios-code-path/bootiful-testing/tree/master/sportsnet-reactive-producer)

* [WebTestClient Documentation](https://docs.spring.io/spring/docs/current/spring-framework-reference/pdf/testing-webtestclient.pdf)

* [Spring Cloud Contract DSL](https://cloud.spring.io/spring-cloud-contract/multi/multi__contract_dsl.html)

* [Fowler on CDC](https://martinfowler.com/articles/consumerDrivenContracts.html)

* Maps are [Injective Functions](https://en.wikipedia.org/wiki/Injective_function) f(x) -> y for any value of x and y

* Are pretty [Constructable numbers](http://www.cut-the-knot.org/arithmetic/constructibleExamples.shtml) interesting
