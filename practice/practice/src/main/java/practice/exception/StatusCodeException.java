package practice.exception;

public class StatusCodeException extends RuntimeException {
    public StatusCodeException(String errorMessage) {
        super(errorMessage);
    }
}
