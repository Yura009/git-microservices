package song.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import song.dto.SongIdResponseDto;
import song.dto.SongIdsResponseDto;
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
    public ResponseEntity<SongIdResponseDto> createSong(@Valid @RequestBody SongDto songDto) {
        Long songId = service.create(songDto).getId();
        return ResponseEntity.ok(new SongIdResponseDto(songId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SongDto> getSong(@PathVariable Long id) {
        SongDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping
    public ResponseEntity<SongIdsResponseDto> deleteSong(
            @RequestParam
            @NotBlank(message = "Invalid ID format: 'V'. Only positive integers are allowed")
            @Size(max = 200, message = "CSV string is too long: received 208 characters, maximum allowed is 200")
            @Pattern(
                    regexp = "^\\d+(,\\d+)*$",
                    message = "Invalid ID format: 'V'. Only positive integers are allowed")
            String id) {
        List<Long> deletedIds = service.deleteByCsv(id);
        return ResponseEntity.ok(new SongIdsResponseDto(deletedIds));
    }
}
