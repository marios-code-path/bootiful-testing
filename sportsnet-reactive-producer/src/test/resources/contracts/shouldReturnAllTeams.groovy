import org.springframework.cloud.contract.spec.Contract
import org.springframework.cloud.contract.spec.internal.HttpMethods

Contract.make {
    description "A Contract for all teams"
    request {
        method HttpMethods.HttpMethod.GET
        url "/teams/all"
    }
    response {
        body(
        """
            [ 
            { "id": "1883", "name" : "Dodgers" },
            { "id": "1912", "name" : "OldSox" }
            ] 
        """
        )
        status(200)
        headers {
                    contentType(applicationJsonUtf8())
        }
    }
}