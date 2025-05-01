package com.yuriimoroziuk.training.resourceservice.service;

import com.yuriimoroziuk.training.resourceservice.client.SongServiceClient;
import com.yuriimoroziuk.training.resourceservice.entity.Mp3Resource;
import com.yuriimoroziuk.training.resourceservice.repository.Mp3ResourceRepository;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class Mp3ResourceService {
    private final Mp3ResourceRepository repository;
    private final SongServiceClient songClient;

    public Mp3ResourceService(Mp3ResourceRepository repository, SongServiceClient songClient) {
        this.repository = repository;
        this.songClient = songClient;
    }

    public Long save(MultipartFile file) throws IOException, TikaException, SAXException {
        if (!file.getContentType().equals("audio/mpeg")) {
            throw new InvalidFileNameException("Invalid MP3 file:", file.getName());
        }
        Mp3Resource resource = new Mp3Resource();
        resource.setFileName(file.getOriginalFilename());
        resource.setFileData(file.getBytes());
        Mp3Resource saved = repository.save(resource);

        Map<String, String> tags = extractTags(file.getInputStream());
        songClient.createSongMetadata(saved.getId(), tags);
        return saved.getId();
    }

    private Map<String, String> extractTags(InputStream inputStream) throws IOException, TikaException, SAXException {
        AutoDetectParser parser = new AutoDetectParser();
        Metadata metadata = new Metadata();

        parser.parse(inputStream, new BodyContentHandler(), metadata, new ParseContext());

        Map<String, String> tags = new HashMap<>();
        tags.put("name", metadata.get("title"));
        tags.put("artist", metadata.get("xmpDM:artist"));
        tags.put("album", metadata.get("xmpDM:album"));
        tags.put("duration", formatDuration(metadata.get("xmpDM:duration")));
        tags.put("year", metadata.get("xmpDM:releaseDate"));

        return tags;
    }

    private String formatDuration(String durationSeconds) {
        if (durationSeconds == null) return "00:00";
        double seconds = Double.parseDouble(durationSeconds);
        int mins = (int) (seconds / 60);
        int secs = (int) (seconds % 60);
        return String.format("%02d:%02d", mins, secs);
    }
}
