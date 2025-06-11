package resource.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import resource.exception.S3FileDeleteException;
import resource.exception.S3FileReadException;
import resource.exception.S3FileSaveException;
import resource.service.CloudStorageService;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3CloudStorageServiceImpl implements CloudStorageService {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket-name}")
    private String bucketName;

    @Override
    public void uploadFile(String key, byte[] data) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType("audio/mpeg")
                .build();

        try {
            s3Client.putObject(request, RequestBody.fromBytes(data));
        } catch (S3Exception ex) {
            log.error("Failed to upload file to S3", ex);
            throw new S3FileSaveException("Failed to upload file to S3");
        }
    }

    @Override
    public byte[] downloadFile(String key) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        try (ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(request)) {
            return s3Object.readAllBytes();
        } catch (IOException | S3Exception ex) {
            log.error("Failed to read file from S3", ex);
            throw new S3FileReadException("Failed to read file from S3");
        }
    }

    @Override
    public void deleteFile(String key) {
        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        try {
            s3Client.deleteObject(request);
        } catch (S3Exception ex) {
            log.error("Failed to delete file from S3", ex);
            throw new S3FileDeleteException("Failed to delete file from S3");
        }
    }
}
