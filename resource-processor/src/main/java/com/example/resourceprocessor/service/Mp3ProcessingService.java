package com.example.resourceprocessor.service;

import com.example.resourceprocessor.dto.SongDto;
import com.example.resourceprocessor.exception.S3FileProcessException;
import lombok.RequiredArgsConstructor;
import org.apache.tika.exception.TikaException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class Mp3ProcessingService {

    @Value("${cloud.aws.s3.bucket-name}")
    private String bucketName;

    private final S3Client s3Client;
    private final Mp3MetadataExtractorService extractorService;

    public SongDto process(String fileName) {
        try {
            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            byte[] bytes = s3Client.getObjectAsBytes(request).asByteArray();
            return extractorService.extractTags(bytes);
        } catch (TikaException | IOException | SAXException ex) {
            throw new S3FileProcessException("Failed to process file from S3");
        }
    }
}
