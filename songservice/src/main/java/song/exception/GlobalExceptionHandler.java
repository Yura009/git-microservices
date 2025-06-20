package song.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SongNotFoundException.class)
    public ResponseEntity<ErrorDto> handleNotFound(SongNotFoundException ex) {
        log.error("Exception occurred: {}", ex.getMessage(), ex);
        ErrorDto errorDto = ErrorDto.builder()
                .errorMessage(ex.getMessage())
                .errorCode(String.valueOf(HttpStatus.NOT_FOUND.value()))
                .build();
        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDto> handleBadRequest(BadRequestException ex) {
        log.error("Exception occurred: {}", ex.getMessage(), ex);
        ErrorDto errorDto = ErrorDto.builder()
                .errorMessage(ex.getMessage())
                .errorCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .build();
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorDto> handleConflict(ConflictException ex) {
        log.error("Exception occurred: {}", ex.getMessage(), ex);
        ErrorDto errorDto = ErrorDto.builder()
                .errorMessage(ex.getMessage())
                .errorCode(String.valueOf(HttpStatus.CONFLICT.value()))
                .build();
        return new ResponseEntity<>(errorDto, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDto> handleConstraintViolation(ConstraintViolationException ex) {
        log.error("Exception occurred: {}", ex.getMessage(), ex);
        ErrorDto errorDto = ErrorDto.builder()
                .errorMessage(ex.getMessage())
                .errorCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .build();
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorDto> handleNotValidArgument(MethodArgumentNotValidException ex) {
        log.error("Exception occurred: {}", ex.getMessage(), ex);
        Map<String, String> errorDetails = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errorDetails.put(error.getField(), error.getDefaultMessage());
        }
        ValidationErrorDto validationErrorDto = ValidationErrorDto.builder()
                .errorMessage("Validation error")
                .errorCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .details(errorDetails)
                .build();
        return new ResponseEntity<>(validationErrorDto, HttpStatus.BAD_REQUEST);
    }
}
