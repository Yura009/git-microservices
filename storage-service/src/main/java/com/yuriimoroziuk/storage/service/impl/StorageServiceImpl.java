package com.yuriimoroziuk.storage.service.impl;

import com.yuriimoroziuk.storage.dto.StorageDto;
import com.yuriimoroziuk.storage.entity.Storage;
import com.yuriimoroziuk.storage.repository.StorageRepository;
import com.yuriimoroziuk.storage.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    private final StorageRepository repository;
    private final ModelMapper mapper;

    @Override
    public StorageDto createStorage(StorageDto dto) {
        Storage storage = mapper.map(dto, Storage.class);
        Storage saved = repository.save(storage);
        return mapper.map(saved, StorageDto.class);
    }

    @Override
    public List<StorageDto> getAllStorages() {
        return repository.findAll().stream()
                .map(s -> mapper.map(s, StorageDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> deleteStorages(List<Long> ids) {
        List<Long> deleted = new ArrayList<>();
        for (Long id : ids) {
            if (repository.existsById(id)) {
                repository.deleteById(id);
                deleted.add(id);
            }
        }
        return deleted;
    }
}
