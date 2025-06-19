package resource.cucumber;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import resource.repository.Mp3ResourceRepository;
import resource.service.CloudStorageService;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UploadMp3StepDefinitions extends CucumberSpringConfiguration {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Mp3ResourceRepository repository;

    @Mock
    private CloudStorageService storageService;

    private byte[] testFile;

    @Given("a valid MP3 file")
    public void a_valid_mp3_file() throws Exception {
        testFile = Files.readAllBytes(Paths.get("src/test/resources/valid-sample-with-required-tags.mp3"));
    }

    @When("the client uploads the file")
    public void the_client_uploads_the_file() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart("/resources")
                        .file("file", testFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
    }

    @Then("the file is saved in cloud storage")
    public void the_file_is_saved_in_cloud_storage() {
        verify(storageService, atLeastOnce()).uploadFile(any(), eq(testFile));
    }

    @Then("metadata is saved in the database")
    public void metadata_is_saved_in_the_database() {
        assertFalse(repository.findAll().isEmpty());
    }
}
