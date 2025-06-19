package com.example.resourceprocessor.cotracts;

import com.example.resourceprocessor.ResourceProcessorApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ResourceProcessorApplication.class)
@AutoConfigureStubRunner(ids = "com.yuriimoroziuk:song-service:+:8082",
        stubsMode = StubRunnerProperties.StubsMode.LOCAL)
@ExtendWith(OutputCaptureExtension.class)
public class ResourceProcessorContractTest {
    private static final String GET_SONG = "http://localhost:8082/songs/1";

    private final RestTemplate restTemplate = new RestTemplate();


    @Test
    void shouldGetSongById(CapturedOutput output) {
        ResponseEntity<String> response = restTemplate.getForEntity(GET_SONG, String.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).contains("Test Name");
        assertThat(response.getBody()).contains("Test Album");
        assertThat(response.getBody()).contains("Test Artist");
        assertThat(response.getBody()).contains("05:05");
        assertThat(response.getBody()).contains("2025");
    }
}
