# Bootiful Reactive Testing

Generate me by the following [link](https://start.spring.io/#!type=maven-project&language=kotlin&platformVersion=2.2.1.RELEASE&packaging=jar&jvmVersion=1.8&groupId=com.testing.r2dbc&artifactId=service&name=service&description=Demo%20project%20for%20Spring%20Boot&packageName=com.testing.r2dbc.service&dependencies=webflux,data-r2dbc,h2,cloud-contract-verifier)

## R2dbc

Connect to h2 database provided in POM

## WebFlux

Exposes our service layer as a web-flux messaging endpoint


## Consumer Driven Contract (Verifier)

Exposes our web-flux endpoints as 'living' contracts that other developers can easily
standup and execute integration tests on.
 
