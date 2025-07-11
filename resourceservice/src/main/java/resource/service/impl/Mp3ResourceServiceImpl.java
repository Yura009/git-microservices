package resource.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import resource.dto.Mp3ResourceDto;
import resource.dto.ResourceMessageDto;
import resource.entity.Mp3Resource;
import resource.exception.ResourceNotFoundException;
import resource.messaging.ResourceMessageSender;
import resource.repository.Mp3ResourceRepository;
import resource.service.CloudStorageService;
import resource.service.Mp3ResourceService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class Mp3ResourceServiceImpl implements Mp3ResourceService {
    private final Mp3ResourceRepository repository;
    private final ModelMapper modelMapper;
    private final CloudStorageService storageService;
    private final ResourceMessageSender resourceMessageSender;

    @Transactional
    @Override
    public Mp3ResourceDto save(byte[] file) {
        String name = UUID.randomUUID().toString();

        storageService.uploadFile(name, file);

        Mp3Resource mp3Resource = new Mp3Resource();
        mp3Resource.setName(name);
        Mp3Resource saved = repository.save(mp3Resource);

        resourceMessageSender.sendResourceUploaded(new ResourceMessageDto(saved.getId().toString()));

        return modelMapper.map(saved, Mp3ResourceDto.class);
    }

    @Override
    public byte[] getFileDataById(Long id) {
        Mp3Resource resource = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource with ID=" + id + " not found"));

        return storageService.downloadFile(resource.getName());
    }

    @Override
    public List<Long> deleteByIds(List<Long> ids) {
        List<Long> deleted = new ArrayList<>();
        for (Long id : ids) {
            Optional<Mp3Resource> optional = repository.findById(id);
            if (optional.isPresent()) {
                Mp3Resource resource = optional.get();
                storageService.deleteFile(resource.getName());
                repository.deleteById(id);
                resourceMessageSender.sendResourceDeleted(new ResourceMessageDto(id.toString()));
                deleted.add(id);
            }
        }
        return deleted;
    }
}