package song.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import song.dto.SongDto;
import song.entity.Song;
import song.exception.BadRequestException;
import song.exception.ConflictException;
import song.mapper.SongMapper;
import song.repository.SongRepository;
import song.service.SongService;

import java.util.List;
import java.util.stream.Stream;

@Service
public class SongServiceImpl implements SongService {

    private final SongRepository repository;
    private final SongMapper mapper;
    private final RestTemplate restTemplate;
    private final String resourceServiceUrl = "http://localhost:8081/recources/";

    public SongServiceImpl(SongRepository repository, SongMapper mapper, RestTemplate restTemplate) {
        this.repository = repository;
        this.mapper = mapper;
        this.restTemplate = restTemplate;
    }

    @Override
    public SongDto create(SongDto dto) {
        Long resourceId = dto.getId();

        try {
            restTemplate.getForEntity(resourceServiceUrl + resourceId, byte[].class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new BadRequestException("Resource with ID=" + resourceId + " does not exist");
        }

        if (repository.existsById(resourceId)) {
            throw new ConflictException("Metadata for this resource already exists");
        }
        Song entity = mapper.toEntity(dto);
        repository.save(entity);
        return dto;
    }

    @Override
    public SongDto getById(Long id) {
        Song song = repository.findById(id).orElseThrow(() -> new RuntimeException("Song not found"));
        return mapper.toDto(song);
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
