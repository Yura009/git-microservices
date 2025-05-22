package com.example.resourceprocessor.service;

import com.example.resourceprocessor.dto.SongDto;
import com.example.resourceprocessor.exception.S3FileProcessException;
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
            log.error("Failed to process file from S3", ex);
            throw new S3FileProcessException("Failed to process file from S3");
        }
    }
}
