package desafio.nathan.StockApp.application.exceptions;

public class InternalServerException extends ApplicationException{
    public InternalServerException(String message) {
        super(message);
    }


    public InternalServerException(String message, Throwable cause) {

        super(message, cause);
    }
}
