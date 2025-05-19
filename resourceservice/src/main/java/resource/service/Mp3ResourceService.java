package resource.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import resource.dto.Mp3ResourceDto;
import resource.entity.Mp3Resource;
import resource.exception.ResourceNotFoundException;
import resource.exception.S3FileReadException;
import resource.repository.Mp3ResourceRepository;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class Mp3ResourceService {
    private final Mp3ResourceRepository repository;
    private final ModelMapper modelMapper;
    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket-name}")
    private String bucketName;

    @Transactional
    public Mp3ResourceDto save(byte[] file) {
        String fileName = UUID.randomUUID().toString();
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType("audio/mpeg")
                .build();

        s3Client.putObject(request, RequestBody.fromBytes(file));

        Mp3Resource mp3Resource = new Mp3Resource();
        mp3Resource.setName(fileName);
        Mp3Resource saved = repository.save(mp3Resource);
        return modelMapper.map(saved, Mp3ResourceDto.class);
    }

    public byte[] getFileDataById(Long id) {
        Mp3Resource resource = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource with ID=" + id + " not found"));

        String fileName = resource.getName();

        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        try (ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(request)) {
            return s3Object.readAllBytes();
        } catch (IOException ex) {
            throw new S3FileReadException("Failed to read file from S3");
        }
    }

    public List<Long> deleteByIds(List<Long> ids) {
        List<Long> deleted = new ArrayList<>();
        for (Long id : ids) {
            Optional<Mp3Resource> optional = repository.findById(id);
            if (optional.isPresent()) {
                Mp3Resource resource = optional.get();
                String fileName = resource.getName();

                DeleteObjectRequest request = DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .build();

                s3Client.deleteObject(request);

                repository.deleteById(id);

                deleted.add(id);
            }
        }
        return deleted;
    }
}
