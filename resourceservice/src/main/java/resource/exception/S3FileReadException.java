package resource.exception;

public class S3FileReadException extends RuntimeException {
    public S3FileReadException(String message) {
        super(message);
    }
}
