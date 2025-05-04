package resource.controller;

import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import resource.dto.DeleteIdsResponseDto;
import resource.dto.Mp3ResourceDto;
import resource.service.Mp3ResourceService;

import java.util.Arrays;
import java.util.List;

@Validated
@RestController
@RequestMapping("resources")
@RequiredArgsConstructor
public class Mp3ResourceController {
    private final Mp3ResourceService service;

    @PostMapping(consumes = "audio/mpeg")
    public ResponseEntity<Mp3ResourceDto> upload(@RequestBody byte[] file) {
        Mp3ResourceDto mp3ResourceDto = service.save(file);
        return ResponseEntity.ok(mp3ResourceDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getResource(
            @PathVariable
            @NotNull
            @Positive
            Long id) {
        byte[] fileData = service.getFileDataById(id);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"resource_" + id + ".mp3\"")
                .header("Content-Type", "audio/mpeg")
                .body(fileData);
    }

    @DeleteMapping
    public ResponseEntity<DeleteIdsResponseDto> deleteResources(
            @RequestParam(name = "id")
            @NotBlank
            @Size(max = 200)
            @Pattern(regexp = "^\\d+(,\\d+)*$", message = "Must be a comma-separated list of numeric IDs")
            String id) {
        List<Long> ids = Arrays.stream(id.split(","))
                .map(Long::parseLong)
                .toList();

        List<Long> deletedIds = service.deleteByIds(ids);
        return ResponseEntity.ok(new DeleteIdsResponseDto(deletedIds));
    }
}
