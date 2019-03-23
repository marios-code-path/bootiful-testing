import org.springframework.cloud.contract.spec.Contract
import org.springframework.cloud.contract.spec.internal.HttpMethods

Contract.make {
    description "Ensure a new car id is returned"
    request {
        method HttpMethods.HttpMethod.POST
        url "/new?color=Red"
    }
    response {
        body(
        """12345-67890-07061-97981"""
        )
        status(200)
        headers {
                    contentType(textPlain())
        }
    }
}
