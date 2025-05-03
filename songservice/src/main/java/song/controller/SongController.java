package song.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import song.dto.SongDto;
import song.service.SongService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/songs")
@RequiredArgsConstructor
public class SongController {
    private final SongService service;

    @PostMapping
    public ResponseEntity<Map<String, Long>> createSong(@Valid @RequestBody SongDto songDto){
        Long songId = service.create(songDto).getId();
        return ResponseEntity.ok(Map.of("id", songId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SongDto> getSong(@PathVariable Long id){
        SongDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping
    public ResponseEntity<Map<String, Object>> deleteSong(@RequestParam String id) {
        if (id == null || id.length() >= 200 || !id.matches("^\\d+(,\\d+)*$")) {
            return ResponseEntity.badRequest().build();
        }

        List<Long> deletedIds = service.deleteByCsv(id);
        Map<String, Object> response = new HashMap<>();
        response.put("ids", deletedIds);
        return ResponseEntity.ok(response);
    }
}
