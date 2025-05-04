package song.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import song.dto.SongDto;
import song.entity.Song;
import song.exception.ConflictException;
import song.repository.SongRepository;
import song.service.SongService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {

    private final SongRepository repository;
    private final ModelMapper mapper;


    @Override
    public SongDto create(SongDto dto) {
        Song song = mapper.map(dto, Song.class);
        if (repository.existsById(song.getId())) {
            throw new ConflictException("Metadata for this resource already exists");
        }
        return mapper.map(repository.save(song), SongDto.class);
    }

    @Override
    public SongDto getById(Long id) {
        Song song = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Song metadata with the specified ID does not exist."));
        return mapper.map(song, SongDto.class);
    }

    @Override
    public List<Long> deleteByCsv(String csv) {
        List<Long> ids = Stream.of(csv.split(","))
                .map(Long::parseLong)
                .toList();

        return ids.stream()
                .filter(repository::existsById)
                .peek(repository::deleteById)
                .toList();
    }

}
