package contracts.songservice

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name("get_song_by_id")

    request {
        method 'GET'
        urlPath('/songs/1')
    }

    response {
        status 200
        headers {
            contentType(applicationJson())
        }
        body(
                id: 1,
                name: "Imagine",
                artist: "John Lennon",
                album: "Imagine",
                duration: "03:15",
                year: "1971"
        )
    }
}
