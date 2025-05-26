package com.example.resourceprocessor.service;

import com.example.resourceprocessor.dto.SongDto;
import com.example.resourceprocessor.exception.Mp3ProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.exception.TikaException;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class Mp3ProcessingService {
    private final Mp3MetadataExtractorService extractorService;

    public SongDto getSongMetaData(byte[] data) {
        try {
            return extractorService.extractTags(data);
        } catch (TikaException | IOException | SAXException ex) {
            log.error("Failed to extract MP3 tags", ex);
            throw new Mp3ProcessingException("Failed to extract MP3 tags");
        }
    }
}
