package practice.exception;

public class DataRangeException extends RuntimeException {
    public DataRangeException(String ErrorMessage) {
        super(ErrorMessage);
    }
}
