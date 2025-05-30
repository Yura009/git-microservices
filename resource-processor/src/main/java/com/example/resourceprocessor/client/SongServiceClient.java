package com.example.resourceprocessor.client;

import com.example.resourceprocessor.dto.SongDto;
import com.example.resourceprocessor.exception.ResourceNotFoundException;
import com.example.resourceprocessor.exception.SongMetadataSendException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class SongServiceClient {
    private final RestTemplate restTemplate;

    @Value("${song.service.url}")
    private String songServiceUrl;

    private static final String SONGS_PATH = "/songs";

    @Retryable(
            retryFor = { SongMetadataSendException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000))
    public void sendSongMetadata(SongDto songDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<SongDto> request = new HttpEntity<>(songDto, headers);
        ResponseEntity<Void> response = restTemplate.postForEntity(songServiceUrl + SONGS_PATH, request, Void.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new SongMetadataSendException("Failed to send song metadata.");
        }
    }
}

