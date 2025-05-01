package com.yuriimoroziuk.training.resourceservice.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class SongServiceClient {
    private final RestTemplate restTemplate;
    private final String songServiceUrl = "http://localhost:8082/songs";

    public SongServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void createSongMetadata(Long resourceId, Map<String, String> tags) {
        Map<String, String> payload = new HashMap<>(tags);
        payload.put("id", String.valueOf(resourceId));
        restTemplate.postForEntity(songServiceUrl, payload, Void.class);
    }
}
