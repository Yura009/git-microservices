package song.service.impl;

import song.dto.SongDto;
import song.entity.Song;
import song.exception.BadRequestException;
import song.exception.ConflictException;
import song.mapper.SongMapper;
import song.repository.SongRepository;
import song.service.SongService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class SongServiceImpl implements SongService {

    private final SongRepository repository;
    private final SongMapper mapper;
    private final RestTemplate restTemplate;
    private final String resourceServiceUrl = "http://localhost:8081/resources/";

    public SongServiceImpl(SongRepository repository, SongMapper mapper, RestTemplate restTemplate) {
        this.repository = repository;
        this.mapper = mapper;
        this.restTemplate = restTemplate;
    }

    @Override
    public Long create(SongDto dto) {
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
        return entity.getId();
    }

    @Override
    public SongDto getById(Long id) {
        return null;
    }

    @Override
    public List<Long> deleteByCsv(String csv) {
        return null;
    }
}
