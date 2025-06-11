package com.example.resourceprocessor.service;

import com.example.resourceprocessor.dto.SongDto;
import com.example.resourceprocessor.exception.Mp3ProcessingException;
import lombok.SneakyThrows;
import org.apache.tika.exception.TikaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class Mp3ProcessingServiceTest {

    @Mock
    private Mp3MetadataExtractorService extractorService;

    @InjectMocks
    private Mp3ProcessingService processingService;

    private final byte[] data = "test-data".getBytes();

    @Test
    @SneakyThrows
    void shouldReturnSongDtoWhenExtraction() {
        SongDto expected = new SongDto();
        expected.setName("Test Song");

        when(extractorService.extractTags(data)).thenReturn(expected);

        SongDto actual = processingService.getSongMetaData(data);

        assertEquals(expected.getName(), actual.getName());
        verify(extractorService).extractTags(data);
    }

    @Test
    @SneakyThrows
    void shouldThrowExceptionWhenParseError() {
        when(extractorService.extractTags(data)).thenThrow(new TikaException("Tika error"));

        assertThrows(Mp3ProcessingException.class, () -> processingService.getSongMetaData(data));
    }
}
