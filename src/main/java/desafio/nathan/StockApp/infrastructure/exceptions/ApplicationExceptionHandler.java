package desafio.nathan.StockApp.infrastructure.exceptions;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import desafio.nathan.StockApp.application.exceptions.ApplicationException;
import desafio.nathan.StockApp.application.exceptions.InternalServerException;
import desafio.nathan.StockApp.application.exceptions.NotFoundException;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ApplicationExceptionHandler {


    @ExceptionHandler(value = {NotFoundException.class})
    protected ResponseEntity<List<ErrorDTO>> handleNotFoundException(NotFoundException exception) {
        log.warn("Generated not found error.", exception);
        return createResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(value = {InternalServerException.class})
    protected ResponseEntity<List<ErrorDTO>> handleInternalServerException(InternalServerException exception) {
        log.warn("Generated internal server error.", exception);
        return createResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }


    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<List<ErrorDTO>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ErrorDTO> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(new ErrorDTO(String.format("%s %s", fieldName, errorMessage)));
        });
        return createResponse(HttpStatus.BAD_REQUEST, errors);
    }

    @SneakyThrows
    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            @NonNull HttpMessageNotReadableException exception,
            @NonNull WebRequest request
    ) {
        log.warn("Generated application error", exception);
        String message = "required request body is missing";

        if (exception.getCause() instanceof InvalidFormatException) {
            InvalidFormatException invalidFormatException = (InvalidFormatException) exception.getCause();
            String pathReference = invalidFormatException.getPathReference();
            message = this.extractMessage(pathReference);
        } else if (exception.getCause() instanceof MismatchedInputException) {
            MismatchedInputException mismatchedInputException = (MismatchedInputException) exception.getCause();
            message = this.extractMessage(mismatchedInputException.getMessage());
        } else if (exception.getCause() instanceof JsonParseException) {
            JsonParseException jsonParseException = (JsonParseException) exception.getCause();
            String field = jsonParseException.getProcessor().currentName();
            message = field + " field is not in valid format";
        }

        return ResponseEntity.badRequest().body(List.of(new ErrorDTO(message)));
    }

    @ExceptionHandler(value = {ApplicationException.class})
    protected ResponseEntity<List<ErrorDTO>> handleApplicationException(ApplicationException exception) {
        log.warn("Generated application error", exception);

        return createResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }


    private ResponseEntity<List<ErrorDTO>> createResponse(HttpStatus status, String message) {
        return ResponseEntity
                .status(status)
                .body(List.of(new ErrorDTO(message)));
    }

    private ResponseEntity<List<ErrorDTO>> createResponse(HttpStatus status, List<ErrorDTO> errors) {
        return ResponseEntity
                .status(status)
                .body(errors);
    }

    private String extractMessage(String originalMessage) {
        String partialField = originalMessage.substring(originalMessage.indexOf("\"") + 1);
        String field = partialField.substring(0, partialField.indexOf("\""));

        return String.format("the %s field is not in valid format", field);
    }
}
