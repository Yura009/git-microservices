package resource.exception;

import jakarta.validation.ConstraintViolationException;
import org.hibernate.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(NoSuchElementException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("errorMessage", ex.getMessage());
        body.put("errorCode", "404");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(InvalidMp3Exception.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(InvalidMp3Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("errorMessage", ex.getMessage());
        body.put("errorCode", "400");
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleOtherErrors(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("errorMessage", ex.getMessage());
        body.put("errorCode", "500");
        return ResponseEntity.internalServerError().body(body);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleNotSupportedMediaType(HttpMediaTypeNotSupportedException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("errorMessage", "Invalid file format: " + ex.getContentType() + ". Only MP3 files are allowed");
        body.put("errorCode", "400");
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundResource(ResourceNotFoundException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("errorMessage", ex.getMessage());
        body.put("errorCode", "404");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("errorMessage", ex.getMessage());
        body.put("errorCode", "400");
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler({ MethodArgumentTypeMismatchException.class, TypeMismatchException.class })
    public ResponseEntity<Map<String, String>> handleTypeMismatch(Exception ex) {
        String value = "unknown";
        if (ex instanceof MethodArgumentTypeMismatchException) {
            Object val = ((MethodArgumentTypeMismatchException) ex).getValue();
            value = val.toString();
        }

        Map<String, String> error = new HashMap<>();
        error.put("errorMessage", "Invalid value '" + value + "' for ID. Must be a positive integer");
        error.put("errorCode", "400");

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
