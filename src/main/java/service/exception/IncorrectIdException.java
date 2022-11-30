package service.exception;

public class IncorrectIdException extends RuntimeException {
    public IncorrectIdException() {
    }

    public IncorrectIdException(final String message) {
        super(message);
    }
}
