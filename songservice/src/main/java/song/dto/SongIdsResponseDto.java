package song.dto;

import lombok.Data;

import java.util.List;

@Data
public class SongIdsResponseDto {
    private final List<Long> ids;
}
