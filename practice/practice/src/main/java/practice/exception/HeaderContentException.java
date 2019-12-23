package practice.exception;

public class HeaderContentException extends RuntimeException {
    public HeaderContentException(String errorMessage) {
        super(errorMessage);
    }
}
