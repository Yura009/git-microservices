package song.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import song.dto.SongDto;
import song.entity.Song;
import song.exception.ConflictException;
import song.exception.SongNotFoundException;
import song.repository.SongRepository;
import song.service.impl.SongServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class SongServiceImplTest {
    @Mock
    private SongRepository repository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private SongServiceImpl songService;

    private SongDto songDto;
    private Song song;

    @BeforeEach
    void setup() {
        songDto = new SongDto(
                1L,
                "Test Song",
                "Test Artist",
                "Test Album",
                "03:45",
                "2025");

        song = new Song(
                1L,
                "Test Song",
                "Test Artist",
                "Test Album",
                "03:45",
                "2025");
    }

    @Test
    void shouldCreateSong() {
        when(modelMapper.map(songDto, Song.class)).thenReturn(song);
        when(repository.existsById(song.getId())).thenReturn(false);
        when(repository.save(song)).thenReturn(song);
        when(modelMapper.map(song, SongDto.class)).thenReturn(songDto);

        SongDto result = songService.create(songDto);

        assertEquals(songDto, result);
        verify(repository).save(song);
    }

    @Test
    void shouldThrowExceptionWhenSongExists() {
        when(modelMapper.map(songDto, Song.class)).thenReturn(song);
        when(repository.existsById(song.getId())).thenReturn(true);

        assertThrows(ConflictException.class, () -> songService.create(songDto));
    }

    @Test
    void shouldReturnSongById() {
        when(repository.findById(1L)).thenReturn(Optional.of(song));
        when(modelMapper.map(song, SongDto.class)).thenReturn(songDto);

        SongDto result = songService.getById(1L);

        assertEquals(songDto, result);
    }

    @Test
    void shouldThrowNotFoundWhenSongMissing() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(SongNotFoundException.class, () -> songService.getById(1L));
    }

    @Test
    void shouldDeleteByCsv() {
        when(repository.existsById(1L)).thenReturn(true);
        when(repository.existsById(2L)).thenReturn(false);
        when(repository.existsById(3L)).thenReturn(true);

        List<Long> deleted = songService.deleteByCsv("1,2,3");

        assertEquals(List.of(1L, 3L), deleted);
        verify(repository).deleteById(1L);
        verify(repository).deleteById(3L);
    }
}
