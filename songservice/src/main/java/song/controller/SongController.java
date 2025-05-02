package song.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import song.dto.SongDto;
import song.service.SongService;

@RestController
@RequestMapping("/songs")
@RequiredArgsConstructor
public class SongController {
    private final SongService service;

    @PostMapping
    public ResponseEntity<Long> createSong(@Valid @RequestBody SongDto songDto){
        Long songId = service.create(songDto).getId();
        return ResponseEntity.ok(songId);
    }

}
