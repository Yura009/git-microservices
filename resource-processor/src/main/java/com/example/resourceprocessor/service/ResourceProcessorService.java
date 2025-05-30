package com.example.resourceprocessor.service;


import com.example.resourceprocessor.client.ResourceServiceClient;
import com.example.resourceprocessor.client.SongServiceClient;
import com.example.resourceprocessor.dto.SongDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceProcessorService {
    private final Mp3ProcessingService processingService;
    private final ResourceServiceClient resourceServiceClient;
    private final SongServiceClient songServiceClient;

    @Value("${song.service.url}")
    private String songServiceUrl;

    public void processResource(String resourceId) {
        byte[] resourceFile = resourceServiceClient.getResourceById(resourceId);
        SongDto songDto = processingService.getSongMetaData(resourceFile);
        songDto.setId(Long.valueOf(resourceId));
        songServiceClient.sendSongMetadata(songDto);

        log.info("Metadata processed and sent for resource ID: {}", resourceId);
    }
}
