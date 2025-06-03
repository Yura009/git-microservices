package com.example.resourceprocessor.service;


import com.example.resourceprocessor.client.ResourceServiceClient;
import com.example.resourceprocessor.client.SongServiceClient;
import com.example.resourceprocessor.dto.SongDto;
import com.example.resourceprocessor.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceProcessorService {
    private final Mp3ProcessingService processingService;
    private final ResourceServiceClient resourceServiceClient;
    private final SongServiceClient songServiceClient;

    @Retryable(
            retryFor = {Exception.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public void processResource(String resourceId) {
        byte[] resourceFile = resourceServiceClient.getResourceById(resourceId);
        SongDto songDto = processingService.getSongMetaData(resourceFile);
        songDto.setId(Long.valueOf(resourceId));
        songServiceClient.sendSongMetadata(songDto);

        log.info("Metadata processed and sent for resource ID: {}", resourceId);
    }

    @Recover
    public byte[] recover(Exception ex, String resourceId) {
        log.error("Failed to get resource {} after retries", resourceId, ex);
        throw new ResourceNotFoundException("Resource unavailable");
    }
}
