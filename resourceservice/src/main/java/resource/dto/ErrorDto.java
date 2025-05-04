package resource.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ErrorDto {
    private String errorMessage;
    private String errorCode;
}
