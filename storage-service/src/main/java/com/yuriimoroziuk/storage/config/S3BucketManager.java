package com.yuriimoroziuk.storage.config;

import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class S3BucketManager {
    private final S3Template s3Template;

    public void createBucket(String bucketName) {
        if (!s3Template.bucketExists(bucketName)) {
            s3Template.createBucket(bucketName);
        }
    }
}
