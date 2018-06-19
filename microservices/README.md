# Bootiful Testing with Spring boot Edgeware.SR2!

Largely inspired from the original talk given by @joshlong, and @mariogray. This project guides the user throuh just a few testing tools used to drive quicker and more efficient testing. Lets take a look at whats covered:

* Spring Boot Test Slices
 * JPA
 * WEB/MVC
* AssertJ testing framework
* Consumer Driven Contract testing with Spring Cloud Contract
* WireMock guided client testing

## AssertJ - a favorite assertion famework

When deciding assertions, I like to get as much terse as possible. This means probably
declaring more formal method descriptions, and using more descriptive API. The goal, however is not coding less, rather it's coding in a way that gives me and the reader 
better insight into what the code is going to expect to produce.

AssertJ example:
    Assertions.assertThat(data)
        .as("A Data Object must not be null")
        .isNotNull()
        .hasNoNullFieldsOrProperties();    

This example is a fluent interface[https://en.wikipedia.org/wiki/Fluent_interface] that gives the simplicity of a continuous call chain, and lets me operate on a parameter with
specific assertions that give detail to the test.

## Client Testing with WireMock
WireMock, as the name implies is a service mocking framework that essentially lets you 
create a service behaviour. This means we will programatically provide request parameters and fill in the specific response that cooresponds to our service. It will stand up a real HTTP listener on the port you specify

Utilizing WireMock is as simple as the fluent API it provides. Using the JAVA class support to describe the service/request behaviour you want to express.

WireMock example:
    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
    @AutoConfigureJson
    @AutoConfigureWireMock(port = 8080)
    public class MyWireMockTest {
        @Before
        public void setUp() throws JsonProcessingException {    
            WireMock.stubFor(WireMock.get("/sample")
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE,          
                            MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .withBody("{'foo':'bar'}")));
        }
    }
This example will simple respond to any HTTP/GET request on port 8080 with an dummy JSON payload. We can verify it using standard functional testing methods. To do this, any http client that talks wire HTTP (all of them) for exampole RestTemplate can be used to access our (mocked) HTTP endpoint.


## The JPA Slice

## The Web Slice

## How to fix the (prototypical) impedance mismatch

## CDCT quickie

END.
