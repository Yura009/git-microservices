package com.example.resourceprocessor.client;

import com.example.resourceprocessor.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class ResourceServiceClient {
    private final RestTemplate restTemplate;

    @Value("${resource.service.url}")
    private String resourceServiceUrl;

    private static final String RESOURCES_PATH = "/resources/";

    public byte[] getResourceById(String resourceId) {
        ResponseEntity<byte[]> response =
                restTemplate.getForEntity(resourceServiceUrl + RESOURCES_PATH + resourceId, byte[].class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        throw new ResourceNotFoundException("Failed to found resource with ID: " + resourceId);
    }
}

