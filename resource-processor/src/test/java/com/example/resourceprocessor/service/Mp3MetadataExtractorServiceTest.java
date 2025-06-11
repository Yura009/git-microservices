package com.example.resourceprocessor.service;

import com.example.resourceprocessor.dto.SongDto;
import com.example.resourceprocessor.service.Mp3MetadataExtractorService;
import lombok.SneakyThrows;
import org.apache.tika.exception.ZeroByteFileException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class Mp3MetadataExtractorServiceTest {
    private final Mp3MetadataExtractorService extractorService = new Mp3MetadataExtractorService();

    @Test
    @SneakyThrows
    void shouldExtractMetadata() {
        byte[] mp3Data = Files.readAllBytes(Paths.get("src/test/resources/valid-sample-with-required-tags.mp3"));

        SongDto songDto = extractorService.extractTags(mp3Data);

        assertNotNull(songDto);
        assertFalse(songDto.getName().isBlank());
        assertFalse(songDto.getArtist().isBlank());
        assertFalse(songDto.getAlbum().isBlank());
        assertTrue(songDto.getDuration().matches("\\d{2}:\\d{2}"));
    }

    @Test
    void shouldThrowExceptionOnEmptyInput() {
        byte[] emptyData = new byte[0];

        assertThrows(ZeroByteFileException.class, () -> {
            extractorService.extractTags(emptyData);
        });
    }
}
