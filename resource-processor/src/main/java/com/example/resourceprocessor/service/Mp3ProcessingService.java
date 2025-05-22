package com.example.resourceprocessor.service;

import com.example.resourceprocessor.dto.SongDto;
import com.example.resourceprocessor.exception.S3FileProcessException;
import lombok.RequiredArgsConstructor;
import org.apache.tika.exception.TikaException;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class Mp3ProcessingService {
    private final Mp3MetadataExtractorService extractorService;

    public SongDto getSongMetaData(byte[] data) {
        try {
            return extractorService.extractTags(data);
        } catch (TikaException | IOException | SAXException ex) {
            throw new S3FileProcessException("Failed to process file from S3");
        }
    }
}
