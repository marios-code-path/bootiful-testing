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