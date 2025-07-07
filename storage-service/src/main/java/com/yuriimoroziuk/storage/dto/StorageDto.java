package com.yuriimoroziuk.storage.dto;

import lombok.Data;

@Data
public class StorageDto {
    private Long id;
    private String type;
    private String bucket;
    private String path;
}
