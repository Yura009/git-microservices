package resource.exception;

public class S3FileSaveException extends RuntimeException {
    public S3FileSaveException(String message) {
        super(message);
    }
}
