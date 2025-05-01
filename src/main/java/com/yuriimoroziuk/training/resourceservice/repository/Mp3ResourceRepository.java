package com.yuriimoroziuk.training.resourceservice.repository;

import com.yuriimoroziuk.training.resourceservice.entity.Mp3Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Mp3ResourceRepository extends JpaRepository<Mp3Resource, Long> {}
