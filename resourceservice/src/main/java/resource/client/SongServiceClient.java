package resource.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import resource.dto.SongDto;

@Component
@RequiredArgsConstructor
public class SongServiceClient {
    private final RestTemplate restTemplate;
    @Value("${song.service.url}")
    private String songServiceUrl;

    public void createSongMetadata(SongDto dto) {
        restTemplate.postForEntity(songServiceUrl, dto, Void.class);
    }
}
