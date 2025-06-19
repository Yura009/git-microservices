package com.example.resourceprocessor.service;

import com.example.resourceprocessor.client.ResourceServiceClient;
import com.example.resourceprocessor.client.SongServiceClient;
import com.example.resourceprocessor.dto.SongDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResourceProcessorServiceTest {

    @Mock
    private ResourceServiceClient resourceServiceClient;
    @Mock
    private SongServiceClient songServiceClient;
    @Mock
    private Mp3ProcessingService processingService;

    @InjectMocks
    private ResourceProcessorService resourceProcessorService;


    @Test
    void shouldProcessResourceSuccessfully() {
        String resourceId = "123";
        byte[] fileData = "test-mp3".getBytes();
        SongDto songDto = new SongDto();
        songDto.setName("Test");

        when(resourceServiceClient.getResourceById(resourceId)).thenReturn(fileData);
        when(processingService.getSongMetaData(fileData)).thenReturn(songDto);

        resourceProcessorService.processResource(resourceId);

        verify(resourceServiceClient).getResourceById(resourceId);
        verify(processingService).getSongMetaData(fileData);
        verify(songServiceClient).sendSongMetadata(songDto);
    }
}
