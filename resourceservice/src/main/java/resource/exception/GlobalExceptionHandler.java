package resource.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import resource.dto.ErrorDto;

import java.util.NoSuchElementException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorDto> handleNotFound(NoSuchElementException ex) {
        log.error("Exception occurred: {}", ex.getMessage(), ex);
        ErrorDto errorDto = ErrorDto.builder()
                .errorMessage(ex.getMessage())
                .errorCode(String.valueOf(HttpStatus.NOT_FOUND.value()))
                .build();
        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidMp3Exception.class)
    public ResponseEntity<ErrorDto> handleBadRequest(InvalidMp3Exception ex) {
        log.error("Exception occurred: {}", ex.getMessage(), ex);
        ErrorDto errorDto = ErrorDto.builder()
                .errorMessage(ex.getMessage())
                .errorCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .build();
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleOtherErrors(Exception ex) {
        log.error("Exception occurred: {}", ex.getMessage(), ex);
        ErrorDto errorDto = ErrorDto.builder()
                .errorMessage(ex.getMessage())
                .errorCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .build();
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorDto> handleNotSupportedMediaType(HttpMediaTypeNotSupportedException ex) {
        log.error("Exception occurred: {}", ex.getMessage(), ex);
        ErrorDto errorDto = ErrorDto.builder()
                .errorMessage("Invalid file format: " + ex.getContentType() + ". Only MP3 files are allowed")
                .errorCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .build();
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDto> handleNotFoundResource(ResourceNotFoundException ex) {
        log.error("Exception occurred: {}", ex.getMessage(), ex);
        ErrorDto errorDto = ErrorDto.builder()
                .errorMessage(ex.getMessage())
                .errorCode(String.valueOf(HttpStatus.NOT_FOUND.value()))
                .build();
        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDto> handleConstraintViolation(ConstraintViolationException ex) {
        log.error("Exception occurred: {}", ex.getMessage(), ex);
        String message = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .findFirst()
                .orElse("Validation failed");

        ErrorDto errorDto = ErrorDto.builder()
                .errorMessage(message)
                .errorCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .build();
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, TypeMismatchException.class})
    public ResponseEntity<ErrorDto> handleTypeMismatch(Exception ex) {
        log.error("Exception occurred: {}", ex.getMessage(), ex);
        String value = "unknown";
        if (ex instanceof MethodArgumentTypeMismatchException) {
            Object val = ((MethodArgumentTypeMismatchException) ex).getValue();
            value = val.toString();
        }
        ErrorDto errorDto = ErrorDto.builder()
                .errorMessage("Invalid value '" + value + "' for ID. Must be a positive integer")
                .errorCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .build();
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }
}
