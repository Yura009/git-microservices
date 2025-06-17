package resource.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import resource.dto.Mp3ResourceDto;
import resource.entity.Mp3Resource;
import resource.exception.ResourceNotFoundException;
import resource.messaging.ResourceMessageSender;
import resource.repository.Mp3ResourceRepository;
import resource.service.CloudStorageService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class Mp3ResourceServiceImplTest {

    @Mock
    private Mp3ResourceRepository repository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private CloudStorageService storageService;
    @Mock
    private ResourceMessageSender messageSender;

    @InjectMocks
    private Mp3ResourceServiceImpl mp3ResourceServiceImpl;

    private byte[] fileContent;
    private Long resourceId;
    private String fileName;

    @BeforeEach
    void setUp() {
        fileContent = "dummy_mp3_data".getBytes();
        resourceId = 1L;
        fileName = UUID.randomUUID().toString();
    }

    @Test
    void shouldSaveMp3ResourceAndSendMessage() {
        Mp3Resource mp3Resource = new Mp3Resource();
        mp3Resource.setName(fileName);

        Mp3Resource saved = new Mp3Resource();
        saved.setId(resourceId);
        saved.setName(fileName);

        Mp3ResourceDto expectedDto = new Mp3ResourceDto();
        expectedDto.setId(resourceId);

        when(repository.save(any(Mp3Resource.class))).thenReturn(saved);
        doNothing().when(storageService).uploadFile(any(String.class), any(byte[].class));
        when(modelMapper.map(saved, Mp3ResourceDto.class)).thenReturn(expectedDto);

        Mp3ResourceDto actualDto = mp3ResourceServiceImpl.save(fileContent);

        assertEquals(expectedDto.getId(), actualDto.getId());
        verify(storageService).uploadFile(any(String.class), eq(fileContent));
        verify(repository).save(any(Mp3Resource.class));
        verify(messageSender).sendResourceUploaded(argThat(dto -> dto.getId().equals(resourceId.toString())));
    }

    @Test
    void shouldReturnFileDataWhenResourceExists() {
        Mp3Resource mp3Resource = new Mp3Resource();
        mp3Resource.setId(resourceId);
        mp3Resource.setName(fileName);


        when(repository.findById(resourceId)).thenReturn(Optional.of(mp3Resource));
        when(storageService.downloadFile(fileName)).thenReturn(fileContent);

        byte[] result = mp3ResourceServiceImpl.getFileDataById(resourceId);

        assertArrayEquals(fileContent, result);
    }


    @Test
    void shouldThrowExceptionWhenResourceNotFound() {
        when(repository.findById(resourceId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> mp3ResourceServiceImpl.getFileDataById(resourceId));

        assertEquals("Resource with ID=" + resourceId + " not found", exception.getMessage());
    }

    @Test
    void shouldDeleteExistingResourcesAndReturnDeletedIds() {
        Mp3Resource mp3Resource = new Mp3Resource();
        mp3Resource.setId(resourceId);
        mp3Resource.setName(fileName);

        when(repository.findById(resourceId)).thenReturn(Optional.of(mp3Resource));

        List<Long> result = mp3ResourceServiceImpl.deleteByIds(List.of(resourceId));

        assertEquals(List.of(resourceId), result);
        verify(storageService).deleteFile(fileName);
        verify(repository).deleteById(resourceId);
    }

    @Test
    void shouldSkipNonExistingResources() {
        Long missingId = 99L;

        Mp3Resource mp3Resource = new Mp3Resource();
        mp3Resource.setId(resourceId);
        mp3Resource.setName(fileName);

        when(repository.findById(resourceId)).thenReturn(Optional.of(mp3Resource));
        when(repository.findById(missingId)).thenReturn(Optional.empty());

        List<Long> result = mp3ResourceServiceImpl.deleteByIds(List.of(resourceId, missingId));

        assertEquals(List.of(resourceId), result);
        verify(storageService).deleteFile(fileName);
        verify(repository).deleteById(resourceId);
        verify(repository, never()).deleteById(missingId);
    }


}
