package resource.service.impl;

import lombok.SneakyThrows;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import resource.dto.Mp3ResourceDto;
import resource.exception.ResourceNotFoundException;
import resource.service.Mp3ResourceService;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Mp3ResourceServiceIntegrationTest {

    @Autowired
    private Mp3ResourceService mp3ResourceService;

    private static Long savedResourceId;

    @Test
    @Order(1)
    @SneakyThrows
    void shouldSaveMp3File() {
        byte[] mp3Bytes = Files.readAllBytes(Paths.get("src/test/resources/valid-sample-with-required-tags.mp3"));

        Mp3ResourceDto result = mp3ResourceService.save(mp3Bytes);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        savedResourceId = result.getId();
    }

    @Test
    @Order(2)
    void shouldReturnMp3FileById() {
        byte[] fileData = mp3ResourceService.getFileDataById(savedResourceId);

        assertThat(fileData).isNotNull();
        assertThat(fileData.length).isGreaterThan(0);
    }

    @Test
    @Order(3)
    void shouldDeleteResourceById() {
        List<Long> deleted = mp3ResourceService.deleteByIds(List.of(savedResourceId));

        assertThat(deleted).contains(savedResourceId);
    }

    @Test
    @Order(4)
    void shouldThrowExceptionWhenNotFound() {
        assertThrows(ResourceNotFoundException.class, () -> mp3ResourceService.getFileDataById(savedResourceId));
    }
}
