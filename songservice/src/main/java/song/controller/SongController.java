package song.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import song.dto.DeleteIdsResponseDto;
import song.dto.SongDto;
import song.service.SongService;

import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/songs")
@RequiredArgsConstructor
public class SongController {
    private final SongService service;

    @PostMapping
    public ResponseEntity<Map<String, Long>> createSong(@Valid @RequestBody SongDto songDto) {
        Long songId = service.create(songDto).getId();
        return ResponseEntity.ok(Map.of("id", songId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SongDto> getSong(@PathVariable Long id) {
        SongDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping
    public ResponseEntity<DeleteIdsResponseDto> deleteSong(
            @RequestParam
            @NotBlank(message = "The provided ID is invalid (e.g., contains letters, decimals, is negative, or zero).")
            @Size(max = 200, message = "The provided ID is invalid (e.g., contains letters, decimals, is negative, or zero).")
            @Pattern(
                    regexp = "^\\d+(,\\d+)*$",
                    message = "The provided ID is invalid (e.g., contains letters, decimals, is negative, or zero).")
            String id) {
        List<Long> deletedIds = service.deleteByCsv(id);
        return ResponseEntity.ok(new DeleteIdsResponseDto(deletedIds));
    }
}
