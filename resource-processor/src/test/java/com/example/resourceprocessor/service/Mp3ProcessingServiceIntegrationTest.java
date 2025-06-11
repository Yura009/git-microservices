package com.example.resourceprocessor.service;

import com.example.resourceprocessor.dto.SongDto;
import com.example.resourceprocessor.exception.Mp3ProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class Mp3ProcessingServiceIntegrationTest {

    @Autowired
    private Mp3ProcessingService mp3ProcessingService;

    @Test
    void shouldExtractMetadataFromValidMp3() throws IOException {
        byte[] mp3Bytes = Files.readAllBytes(Paths.get("src/test/resources/valid-sample-with-required-tags.mp3"));

        SongDto songDto = mp3ProcessingService.getSongMetaData(mp3Bytes);

        assertThat(songDto).isNotNull();
        assertThat(songDto.getArtist()).isNotBlank();
        assertThat(songDto.getName()).isNotBlank();
    }
}
