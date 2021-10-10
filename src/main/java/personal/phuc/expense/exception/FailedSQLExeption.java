package personal.phuc.expense.exception;

public class FailedSQLExeption extends RuntimeException {
    public FailedSQLExeption(String message) {
        super(message);
    }

    public FailedSQLExeption(String message, Throwable cause) {
        super(message, cause);
    }
}
