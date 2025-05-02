package song.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(BadRequestException ex){
        Map<String, Object> body = new HashMap<>();
        body.put("errorMessage", ex.getMessage());
        body.put("errorCode", "400");
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Map<String, Object>> handleConflict(ConflictException ex){
        Map<String, Object> body = new HashMap<>();
        body.put("errorMessage", ex.getMessage());
        body.put("errorCode", "409");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }
}
