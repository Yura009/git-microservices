package resource.service;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import resource.client.SongServiceClient;
import resource.entity.Mp3Resource;
import resource.repository.Mp3ResourceRepository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class Mp3ResourceService {
    private final Mp3ResourceRepository repository;
    private final SongServiceClient songClient;

    public Mp3ResourceService(Mp3ResourceRepository repository, SongServiceClient songClient) {
        this.repository = repository;
        this.songClient = songClient;
    }

    public Long save(byte[] file) throws IOException, TikaException, SAXException {
        Mp3Resource resource = new Mp3Resource();
        resource.setFileData(file);
        Mp3Resource saved = repository.save(resource);

        try (InputStream is = new ByteArrayInputStream(file)) {
            Map<String, String> tags = extractTags(is);
            songClient.createSongMetadata(saved.getId(), tags);
        }

        return saved.getId();
    }

    public byte[] getFileDataById(Long id) {
        return repository.findById(id)
                .orElseThrow(NoSuchElementException::new)
                .getFileData();
    }

    public List<Long> deleteByIds(List<Long> ids) {
        List<Long> deleted = new ArrayList<>();
        for (Long id : ids) {
            if (repository.existsById(id)) {
                repository.deleteById(id);
                deleted.add(id);
            }
        }
        return deleted;
    }

    private Map<String, String> extractTags(InputStream inputStream) throws IOException, TikaException, SAXException {
        AutoDetectParser parser = new AutoDetectParser();
        Metadata metadata = new Metadata();
        parser.parse(inputStream, new BodyContentHandler(), metadata, new ParseContext());

        Map<String, String> tags = new HashMap<>();
        tags.put("name", getSafe(metadata, "title"));
        tags.put("artist", getSafe(metadata, "xmpDM:artist"));
        tags.put("album", getSafe(metadata, "xmpDM:album"));
        tags.put("duration", formatDuration(metadata.get("xmpDM:duration")));
        tags.put("year", getSafe(metadata, "xmpDM:releaseDate"));

        return tags;
    }

    private String getSafe(Metadata metadata, String key) {
        String value = metadata.get(key);
        return value != null ? value : "";
    }

    private String formatDuration(String durationSeconds) {
        if (durationSeconds == null || durationSeconds.isBlank()) return "00:00";
        try {
            double seconds = Double.parseDouble(durationSeconds);
            int mins = (int) (seconds / 60);
            int secs = (int) (seconds % 60);
            return String.format("%02d:%02d", mins, secs);
        } catch (NumberFormatException e) {
            return "00:00";
        }
    }
}
