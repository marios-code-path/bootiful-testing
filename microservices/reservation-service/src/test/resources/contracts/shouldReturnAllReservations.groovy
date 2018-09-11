import org.springframework.cloud.contract.spec.Contract
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

Contract.make {
    description "should return all reservations"
    request {
        url "/reservations"
        method GET()
    }
    response {
        status 200
        body([[id: 1L, reservationName: "CAFE"]])
        headers {
            header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
        }
    }
}
/**
 #!/bin/bash

 java -jar stub-runner-boot-1.1.0.RELEASE.jar --stubrunner.workOffline=true --stubrunner.ids=com.example:reservation-service:+:8080

 **/