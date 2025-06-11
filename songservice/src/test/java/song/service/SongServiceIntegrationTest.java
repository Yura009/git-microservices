package song.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import song.dto.SongDto;
import song.exception.ConflictException;
import song.repository.SongRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SongServiceIntegrationTest {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private SongService songService;

    private SongDto songDto;

    @BeforeEach
    void cleanUp() {
        songRepository.deleteAll();
        songDto = new SongDto(
                1L,
                "Test Song",
                "Test Artist",
                "Test Album",
                "03:45",
                "2025");
    }

    @Test
    void shouldCreateSong() {
        SongDto created = songService.create(songDto);
        assertNotNull(created);
        assertEquals("Test Song", created.getName());
    }

    @Test
    void shouldThrowExceptionIfSongAlreadyExists() {
        songService.create(songDto);
        assertThrows(ConflictException.class, () -> songService.create(songDto));
    }
}
