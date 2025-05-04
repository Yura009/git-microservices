package resource.service;

import lombok.RequiredArgsConstructor;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import resource.client.SongServiceClient;
import resource.dto.Mp3ResourceDto;
import resource.dto.SongDto;
import resource.entity.Mp3Resource;
import resource.exception.InvalidMp3Exception;
import resource.exception.ResourceNotFoundException;
import resource.repository.Mp3ResourceRepository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Mp3ResourceService {
    private final Mp3ResourceRepository repository;
    private final SongServiceClient songClient;
    private final ModelMapper modelMapper;

    public Mp3ResourceDto save(byte[] file) {
        Mp3Resource resource = new Mp3Resource();
        resource.setFileData(file);
        Mp3Resource saved = repository.save(resource);
        Mp3ResourceDto mp3ResourceDto = modelMapper.map(saved, Mp3ResourceDto.class);
        try {
            SongDto songDto = extractTags(file);
            songDto.setId(saved.getId());
            songClient.createSongMetadata(songDto);

        } catch (IOException | TikaException | SAXException ex) {
            throw new InvalidMp3Exception("The request body is invalid MP3.");
        }
        return mp3ResourceDto;
    }

    public byte[] getFileDataById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource with ID=" + id + " not found"))
                .getFileData();
    }

    public List<Long> deleteByIds(List<Long> ids) {
        List<Long> deleted = new ArrayList<>();
        for (Long id : ids) {
            if (repository.existsById(id)) {
                repository.deleteById(id);
                songClient.deleteSongByResourceId(id);
                deleted.add(id);
            }
        }
        return deleted;
    }

    private SongDto extractTags(byte[] file) throws IOException, TikaException, SAXException {
        InputStream stream = new ByteArrayInputStream(file);
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        AutoDetectParser parser = new AutoDetectParser();
        ParseContext context = new ParseContext();
        parser.parse(stream, handler, metadata, context);

        SongDto songDto = new SongDto();
        songDto.setName(getSafe(metadata, "dc:title"));
        songDto.setArtist(getSafe(metadata, "xmpDM:artist"));
        songDto.setAlbum(getSafe(metadata, "xmpDM:album"));
        songDto.setDuration(formatDuration(metadata.get("xmpDM:duration")));
        songDto.setYear(getSafe(metadata, "xmpDM:releaseDate"));

        return songDto;
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
