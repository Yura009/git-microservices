package com.yuriimoroziuk.storage.controller;

import com.yuriimoroziuk.storage.dto.StorageDto;
import com.yuriimoroziuk.storage.service.StorageService;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/storages")
@RequiredArgsConstructor
public class StorageController {
    private final StorageService service;

    @PostMapping
    public ResponseEntity<StorageDto> createStorage(@RequestBody StorageDto dto) {
        StorageDto created = service.createStorage(dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<StorageDto>> getAllStorages() {
        return ResponseEntity.ok(service.getAllStorages());
    }

    @DeleteMapping
    public ResponseEntity<List<Long>> deleteStorages(
            @RequestParam("id")
            @Size(max = 200)
            @Pattern(regexp = "^\\d+(,\\d+)*$")
            String ids
    ) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        List<Long> deleted = service.deleteStorages(idList);
        return ResponseEntity.ok(deleted);
    }
}
