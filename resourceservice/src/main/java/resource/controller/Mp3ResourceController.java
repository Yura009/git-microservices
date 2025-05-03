package resource.controller;

import org.apache.tika.exception.TikaException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;
import resource.dto.Mp3ResourceDto;
import resource.service.Mp3ResourceService;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("resources")
public class Mp3ResourceController {
    private final Mp3ResourceService service;

    public Mp3ResourceController(Mp3ResourceService service) {
        this.service = service;
    }

    @PostMapping(consumes = "audio/mpeg")
    public ResponseEntity<Mp3ResourceDto> upload(@RequestBody byte[] file) throws IOException, TikaException, SAXException {
        Long id = service.save(file);
        return ResponseEntity.ok(new Mp3ResourceDto(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getResource(@PathVariable Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().build();
        }

        try {
            byte[] fileData = service.getFileDataById(id);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"resource_" + id + ".mp3\"")
                    .header("Content-Type", "audio/mpeg")
                    .body(fileData);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<Map<String, Object>> deleteResources(@RequestParam String id) {
        if (id == null || id.length() >= 200 || !id.matches("^\\d+(,\\d+)*$")) {
            return ResponseEntity.badRequest().build();
        }

        List<Long> ids = Arrays.stream(id.split(","))
                .map(Long::parseLong)
                .toList();

        List<Long> deletedIds = service.deleteByIds(ids);
        Map<String, Object> response = new HashMap<>();
        response.put("ids", deletedIds);
        return ResponseEntity.ok(response);
    }
}
