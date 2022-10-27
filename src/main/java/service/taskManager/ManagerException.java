package service.taskManager;

public class ManagerException extends RuntimeException {
    public ManagerException() {
    }

    public ManagerException(final String message) {
        super(message);
    }
}
