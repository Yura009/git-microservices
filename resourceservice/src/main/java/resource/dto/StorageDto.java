package resource.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StorageDto {
    private Long id;
    private String storageType;
    private String bucket;
    private String path;
}
