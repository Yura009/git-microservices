package com.yuriimoroziuk.storage.config;

import com.yuriimoroziuk.storage.entity.Storage;
import com.yuriimoroziuk.storage.repository.StorageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StorageInitializer implements CommandLineRunner {

    private final StorageRepository storageRepository;
    private final S3BucketManager s3BucketManager;

    @Override
    public void run(String... args) {
        log.info("Initializing storage configurations...");

        createIfMissing("STAGING", "staging-bucket", "/files");
        createIfMissing("PERMANENT", "permanent-bucket", "/files");

        log.info("Storage initialization complete.");
    }

    private void createIfMissing(String storageType, String bucket, String path) {
        boolean exists = storageRepository.findByTypeAndBucket(storageType, bucket).isPresent();

        if (!exists) {
            log.info("Creating new storage: type={}, bucket={}, path={}", storageType, bucket, path);
            s3BucketManager.createBucket(bucket);

            log.info("Bucket '{}' created (or already exists).", bucket);
            Storage storage = new Storage();
            storage.setType(storageType);
            storage.setBucket(bucket);
            storage.setPath(path);

            storageRepository.save(storage);
            log.info("Storage saved: {}", storage);
        } else {
            log.info("Storage config already exists: type={}, bucket={}", storageType, bucket);
        }
    }
}
