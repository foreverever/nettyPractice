package practice.exception;

public class NotValidLengthFiled extends RuntimeException {
    public NotValidLengthFiled(String errorMessage) {
        super(errorMessage);
    }
}
