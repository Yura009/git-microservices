package com.example.resourceprocessor.service;

import com.example.resourceprocessor.dto.SongDto;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class Mp3MetadataExtractorService {

    public SongDto extractTags(byte[] file) throws IOException, TikaException, SAXException {
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
