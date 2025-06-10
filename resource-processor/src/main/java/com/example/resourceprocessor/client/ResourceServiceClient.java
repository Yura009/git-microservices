package com.example.resourceprocessor.client;

import com.example.resourceprocessor.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResourceServiceClient {
    private final RestTemplate restTemplate;

    @Value("${resource.service.url}")
    private String resourceServiceUrl;

    private static final String RESOURCES_PATH = "/resources/";

    @Retryable(
            retryFor = {Exception.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000))
    public byte[] getResourceById(String resourceId) {
        ResponseEntity<byte[]> response =
                restTemplate.getForEntity(resourceServiceUrl + RESOURCES_PATH + Long.valueOf(resourceId), byte[].class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        throw new ResourceNotFoundException("Failed to found resource with ID: " + resourceId);
    }

    @Recover
    public byte[] recover(Exception ex, String resourceId) {
        log.error("Failed to get resource after retries for ID: " + resourceId, ex);
        throw new ResourceNotFoundException("Resource unavailable");
    }
}

