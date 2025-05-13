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

    private static final String SONGS_PATH = "/songs";

    public void createSongMetadata(SongDto dto) {
        restTemplate.postForEntity(songServiceUrl + SONGS_PATH, dto, Void.class);
    }

    public void deleteSongByResourceId(Long resourceId) {
        restTemplate.delete(songServiceUrl + SONGS_PATH + "?id=" + resourceId);
    }
}

