package com.yuriimoroziuk.training.resourceservice.controller;

import com.yuriimoroziuk.training.resourceservice.dto.ResourceUploadResponse;
import com.yuriimoroziuk.training.resourceservice.service.Mp3ResourceService;
import org.apache.tika.exception.TikaException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import java.io.IOException;

@RestController
@RequestMapping("resources")
public class Mp3ResourceController {
    private final Mp3ResourceService service;

    public Mp3ResourceController(Mp3ResourceService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResourceUploadResponse> upload(@RequestParam("file") MultipartFile file) throws IOException, TikaException, SAXException {
        Long id = service.save(file);
        return ResponseEntity.ok(new ResourceUploadResponse(id));
    }
}
