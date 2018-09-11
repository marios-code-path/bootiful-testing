import org.springframework.cloud.contract.spec.Contract
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

Contract.make {
    description "Contracts for Finding all Teams"

    request {
        url "/team/all"
        method  GET()
    }

    response {
        status  200
        headers {
            header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
        }
        body([[id: 1L, name: "RANGERS"], [id: 2L, name: "ASTROS"]])
    }
}