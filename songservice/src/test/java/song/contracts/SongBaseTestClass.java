package song.contracts;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import song.dto.SongDto;
import song.service.SongService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
public abstract class SongBaseTestClass {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected SongService songService;

    protected final SongDto exampleSong = SongDto.builder()
            .id(1L)
            .name("Imagine")
            .artist("John Lennon")
            .album("Imagine")
            .duration("03:15")
            .year("1971")
            .build();

    @BeforeEach
    void setupStubs() {
        when(songService.create(any(SongDto.class))).thenReturn(exampleSong);
        when(songService.getById(1L)).thenReturn(exampleSong);
        when(songService.deleteByCsv("1")).thenReturn(List.of(1L));
    }
}
