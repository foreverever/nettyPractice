package practice.exception;

public class NotValidStatusCodeException extends RuntimeException {
    public NotValidStatusCodeException(String message) {
        super(message);
    }
}