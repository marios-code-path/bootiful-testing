package com.example.producer

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@Configuration
class CarRestService {

    @Bean
    fun routes(carService: CarService): RouterFunction<ServerResponse> = router {
        POST("/new") { req ->
            ServerResponse
                    .ok()
                    .body(carService.newCar(
                            Car("Tesla", "Model Z", req.queryParam("color").orElse("Red"))),
                            String::class.java)
        }

        GET("/car") { req ->
            ServerResponse
                    .ok()
                    .body(carService
                            .getCar(req
                                    .queryParam("key")
                                    .orElseThrow { Exception("no car found") }
                            ),
                            Car::class.java)
        }
    }

}
