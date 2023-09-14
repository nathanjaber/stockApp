package desafio.nathan.StockApp.infrastructure.exceptions;

import desafio.nathan.StockApp.entity.Stock;

import javax.validation.ConstraintViolation;
import java.util.Set;

public class ValidationExceptiion extends Throwable {
    public ValidationExceptiion(Set<ConstraintViolation<Stock>> violations) {
    }
}
