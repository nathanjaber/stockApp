package desafio.nathan.StockApp.application.exceptions;

public class InvalidTypeException extends ApplicationException {
    public InvalidTypeException(String message) {
        super(message);
    }

    public InvalidTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
