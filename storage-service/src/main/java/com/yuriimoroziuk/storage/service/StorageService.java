package com.yuriimoroziuk.storage.service;

import com.yuriimoroziuk.storage.dto.StorageDto;

import java.util.List;

public interface StorageService {
    StorageDto createStorage(StorageDto dto);
    List<StorageDto> getAllStorages();
    List<Long> deleteStorages(List<Long> ids);

}
