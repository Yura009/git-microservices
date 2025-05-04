package song.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SongNotFoundException.class)
    public ResponseEntity<ErrorDto> handleNotFound(SongNotFoundException ex) {
        ErrorDto errorDto = ErrorDto.builder()
                .errorMessage(ex.getMessage())
                .errorCode(String.valueOf(HttpStatus.NOT_FOUND.value()))
                .build();
        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDto> handleBadRequest(BadRequestException ex) {
        ErrorDto errorDto = ErrorDto.builder()
                .errorMessage(ex.getMessage())
                .errorCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .build();
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorDto> handleConflict(ConflictException ex) {
        ErrorDto errorDto = ErrorDto.builder()
                .errorMessage(ex.getMessage())
                .errorCode(String.valueOf(HttpStatus.CONFLICT.value()))
                .build();
        return new ResponseEntity<>(errorDto, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationErrorDto> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(cv -> {
            String path = cv.getPropertyPath().toString();
            String field = path.contains(".") ? path.substring(path.lastIndexOf('.') + 1) : path;
            errors.put(field, cv.getMessage());
        });

        ValidationErrorDto validationErrorDto = ValidationErrorDto.builder()
                .errorMessage(ex.getMessage())
                .errorCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .details(errors)
                .build();
        return new ResponseEntity<>(validationErrorDto, HttpStatus.BAD_REQUEST);
    }
}
