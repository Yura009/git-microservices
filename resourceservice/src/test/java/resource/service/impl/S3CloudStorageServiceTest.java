package resource.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import resource.exception.S3FileDeleteException;
import resource.exception.S3FileReadException;
import resource.exception.S3FileSaveException;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class S3CloudStorageServiceTest {
    @Mock
    private S3Client s3Client;

    @InjectMocks
    private S3CloudStorageServiceImpl storageService;

    private byte[] data;
    private String key;

    @BeforeEach
    void setup() {
        storageService = new S3CloudStorageServiceImpl(s3Client);
        ReflectionTestUtils.setField(storageService, "bucketName", "test-bucket");
        data = "test-data".getBytes();
        key = "test=key";
    }

    @Test
    void shouldUploadFile() {
        assertDoesNotThrow(() -> storageService.uploadFile(key, data));

        verify(s3Client, times(1))
                .putObject(any(PutObjectRequest.class), any(RequestBody.class));
    }

    @Test
    void shouldThrowExceptionWhenUploadMissingFile() {
        doThrow(S3Exception.builder().message("Upload fail").build())
                .when(s3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));

        assertThrows(S3FileSaveException.class, () -> storageService.uploadFile(key, data));
    }

    @Test
    void shouldDownloadFile() {
        ResponseInputStream<GetObjectResponse> mockResponseStream =
                new ResponseInputStream<>(GetObjectResponse.builder().build(), new ByteArrayInputStream(data));

        when(s3Client.getObject(any(GetObjectRequest.class))).thenReturn(mockResponseStream);

        byte[] actual = storageService.downloadFile(key);
        assertArrayEquals(data, actual);
    }

    @Test
    void shouldThrowExceptionWhenDownloadMissingFile() {
        when(s3Client.getObject(any(GetObjectRequest.class)))
                .thenThrow(S3Exception.builder().message("Download fail").build());

        assertThrows(S3FileReadException.class, () -> storageService.downloadFile(key));
    }

    @Test
    void shouldDeleteFile() {
        assertDoesNotThrow(() -> storageService.deleteFile(key));
        verify(s3Client).deleteObject(any(DeleteObjectRequest.class));
    }

    @Test
    void shouldThrowExceptionWhenDeleteMissingFile() {
        when(s3Client.deleteObject(any(DeleteObjectRequest.class)))
                .thenThrow(S3Exception.builder().message("Delete fail").build());

        assertThrows(S3FileDeleteException.class, () -> storageService.deleteFile(key));
    }
}
