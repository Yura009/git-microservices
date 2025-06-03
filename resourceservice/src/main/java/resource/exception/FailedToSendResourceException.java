package resource.exception;

public class FailedToSendResourceException extends RuntimeException {
    public FailedToSendResourceException(String message) {
        super(message);
    }
}
