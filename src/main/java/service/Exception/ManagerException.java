package service.Exception;

public class ManagerException extends RuntimeException {
    public ManagerException() {
    }

    public ManagerException(final String message) {
        super(message);
    }
}
