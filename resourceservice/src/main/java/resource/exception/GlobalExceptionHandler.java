package resource.exception;

import jakarta.validation.ConstraintViolationException;
import org.hibernate.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import resource.dto.ErrorDto;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorDto> handleNotFound(NoSuchElementException ex) {
        ErrorDto errorDto = ErrorDto.builder()
                .errorMessage(ex.getMessage())
                .errorCode("404")
                .build();
        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidMp3Exception.class)
    public ResponseEntity<ErrorDto> handleBadRequest(InvalidMp3Exception ex) {
        ErrorDto errorDto = ErrorDto.builder()
                .errorMessage(ex.getMessage())
                .errorCode("400")
                .build();
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleOtherErrors(Exception ex) {
        ErrorDto errorDto = ErrorDto.builder()
                .errorMessage(ex.getMessage())
                .errorCode("500")
                .build();
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorDto> handleNotSupportedMediaType(HttpMediaTypeNotSupportedException ex) {
        ErrorDto errorDto = ErrorDto.builder()
                .errorMessage("Invalid file format: " + ex.getContentType() + ". Only MP3 files are allowed")
                .errorCode("400")
                .build();
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDto> handleNotFoundResource(ResourceNotFoundException ex) {
        ErrorDto errorDto = ErrorDto.builder()
                .errorMessage(ex.getMessage())
                .errorCode("404")
                .build();
        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDto> handleConstraintViolation(ConstraintViolationException ex) {
        ErrorDto errorDto = ErrorDto.builder()
                .errorMessage(ex.getMessage())
                .errorCode("400")
                .build();
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, TypeMismatchException.class})
    public ResponseEntity<ErrorDto> handleTypeMismatch(Exception ex) {
        String value = "unknown";
        if (ex instanceof MethodArgumentTypeMismatchException) {
            Object val = ((MethodArgumentTypeMismatchException) ex).getValue();
            value = val.toString();
        }
        ErrorDto errorDto = ErrorDto.builder()
                .errorMessage("Invalid value '" + value + "' for ID. Must be a positive integer")
                .errorCode("400")
                .build();
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }
}
