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
    private static final String SONG_SERVICE_BASE_URL = "http://song-service/songs";

    public void createSongMetadata(SongDto dto) {
        restTemplate.postForEntity(SONG_SERVICE_BASE_URL, dto, Void.class);
    }

    public void deleteSongByResourceId(Long resourceId) {
        restTemplate.delete(SONG_SERVICE_BASE_URL + "?id=" + resourceId);
    }
}
