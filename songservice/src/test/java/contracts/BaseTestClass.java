package contracts;


import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;
import song.SongServiceApplication;
import song.controller.SongController;
import song.dto.SongDto;
import song.service.impl.SongServiceImpl;

@SpringBootTest(classes = SongServiceApplication.class)
public class BaseTestClass {

    @Mock
    private SongServiceImpl songService;

    @BeforeEach
    void setup() {
        StandaloneMockMvcBuilder standaloneMockMvcBuilder = MockMvcBuilders.standaloneSetup(new SongController(songService));
        RestAssuredMockMvc.standaloneSetup(standaloneMockMvcBuilder);
        SongDto songDto = SongDto.builder()
                .id(1L)
                .name("Test Name")
                .album("Test Album")
                .artist("Test Artist")
                .duration("05:05")
                .year("2025").build();
        Mockito.when(songService.getById(Mockito.any(Long.class))).thenReturn(songDto);
    }
}