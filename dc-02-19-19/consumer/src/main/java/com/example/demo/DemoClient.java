package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public class DemoClient {
  private final WebClient webClient;

  public DemoClient(WebClient client) {
    this.webClient = client;
  }

  @Value("${server.url:http://localhost:8090}")
  String baseUri;

  public Flux<Person> getAll() {
    return webClient.get().uri(baseUri + "/all").retrieve().bodyToFlux(Person.class);
  }
}
