package resource.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ResourceMessageDto implements Serializable {
    private String id;
}
