package resource.dto;

import lombok.Data;

import java.util.List;

@Data
public class DeleteIdsResponseDto {
    private final List<Long> ids;
}
