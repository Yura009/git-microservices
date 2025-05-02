package song.service;

import song.dto.SongDto;

import java.util.List;

public interface SongService {
    SongDto create(SongDto dto);
    SongDto getById(Long id);
    List<Long> deleteByCsv(String csv);
}
