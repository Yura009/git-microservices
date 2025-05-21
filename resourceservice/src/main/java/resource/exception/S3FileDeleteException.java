package resource.exception;

public class S3FileDeleteException extends RuntimeException {
    public S3FileDeleteException(String message) {
        super(message);
    }
}
