package resource.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import resource.dto.SongDto;

import java.util.HashMap;
import java.util.Map;

@Component
public class SongServiceClient {
    private final RestTemplate restTemplate;
    private final String songServiceUrl = "http://localhost:8082/songs";

    public SongServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void createSongMetadata(SongDto dto) {
        restTemplate.postForEntity(songServiceUrl, dto, Void.class);
    }
}
