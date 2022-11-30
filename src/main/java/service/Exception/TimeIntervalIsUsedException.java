package service.Exception;

public class TimeIntervalIsUsedException extends RuntimeException {
    public void TimeIntervalIsUsedException() {
    }

    public TimeIntervalIsUsedException(final String message) {
        super(message);
    }
}

