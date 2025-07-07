package com.yuriimoroziuk.storage.repository;

import com.yuriimoroziuk.storage.entity.Storage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StorageRepository extends JpaRepository<Storage, Long> {
    Optional<Storage> findByTypeAndBucket(String type, String bucket);

    Optional<Storage> findByType(String type);
}
