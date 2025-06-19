package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Get song by ID"
    request {
        method GET()
        urlPath("/songs/1")
    }
    response {
        status 200
        headers {
            contentType(applicationJson())
        }
        body(
                id: 1,
                name: "Test Name",
                album: "Test Album",
                artist: "Test Artist",
                duration: "05:05",
                year: "2025"
        )
    }
}
