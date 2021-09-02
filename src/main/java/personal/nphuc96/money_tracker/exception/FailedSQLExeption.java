package personal.nphuc96.money_tracker.exception;

public class FailedSQLExeption extends RuntimeException {
    public FailedSQLExeption(String message) {
        super(message);
    }

    public FailedSQLExeption(String message, Throwable cause) {
        super(message, cause);
    }
}
