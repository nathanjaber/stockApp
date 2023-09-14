package desafio.nathan.StockApp.infrastructure.exceptions;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import desafio.nathan.StockApp.application.exceptions.ApplicationException;
import desafio.nathan.StockApp.application.exceptions.InvalidTypeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(MockitoExtension.class)
class ApplicationExceptionHandlerTest {

    @Mock
    private TypeDescriptor typeDescriptor;

    @Mock
    private WebRequest webRequest;

    @Mock
    private HttpHeaders httpHeaders;

    @Mock
    private JsonParseException jsonParseException;

    @Mock
    private JsonParser jsonParser;


    private final ApplicationExceptionHandler applicationExceptionHandler = new ApplicationExceptionHandler();

    @Test
    void whenHandleHttpMessageNotReadable_thenReturn400() {
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("any_message");
        ResponseEntity<Object> response = this.applicationExceptionHandler.handleHttpMessageNotReadable(exception, this.webRequest);
        List<ErrorDTO> body = (List<ErrorDTO>) response.getBody();
        assertEquals(400, response.getStatusCodeValue());
        assertNotNull(body.get(0).getMessage());
    }

    @Test
    void whenConversionFailed_thenReturn400() {
        ConversionFailedException conversionFailedException = new ConversionFailedException(null, this.typeDescriptor, null, new Throwable());
        ApplicationException applicationException = new InvalidTypeException("any", conversionFailedException);

        ResponseEntity<List<ErrorDTO>> listResponseEntity =
                this.applicationExceptionHandler.handleApplicationException(applicationException);

        assertEquals(400, listResponseEntity.getStatusCodeValue());
    }
}