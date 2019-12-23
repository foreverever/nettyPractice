package practice.exception;

public class DataContentException extends RuntimeException {
    public DataContentException(String errorMessage) {
        super(errorMessage);
    }
}
