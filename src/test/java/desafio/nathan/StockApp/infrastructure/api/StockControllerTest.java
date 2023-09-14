package desafio.nathan.StockApp.infrastructure.api;

import desafio.nathan.StockApp.application.DTOs.StockCreationDTO;
import desafio.nathan.StockApp.application.DTOs.StockDTO;
import desafio.nathan.StockApp.application.DTOs.StockUpdatePriceDTO;
import desafio.nathan.StockApp.ports.IStockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockControllerTest {

    @Mock
    private IStockService service;

    @InjectMocks
    private StockController controller;

    @BeforeEach
    void setUp() {
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mock(HttpServletRequest.class)));
    }


    @Test
    public void whenGetById_thenReturnResponseEntityOfStockDTO() {
        when(this.service.getById(anyLong())).thenReturn(mock(StockDTO.class));

        ResponseEntity<StockDTO> response = this.controller.get(1L);

        verify(this.service, times(1)).getById(anyLong());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void whenGetAll_thenReturnResponseEntityOfListOfStockDTO() {
        when(this.service.getAll()).thenReturn(List.of(mock(StockDTO.class)));

        ResponseEntity<List<StockDTO>> response = this.controller.getAll();

        verify(this.service, times(1)).getAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void whenCreate_thenReturnResponseEntityVoidWithHeaderLocation() {
        when(this.service.create(any(StockCreationDTO.class))).thenReturn(1L);

        ResponseEntity<Void> response = this.controller.create(mock(StockCreationDTO.class));

        verify(this.service, times(1)).create(any(StockCreationDTO.class));
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNull(response.getBody());
        assertNotNull(response.getHeaders().getLocation());
    }

    @Test
    public void whenUpdatePrice_thenReturnResponseEntityVoidWithHeaderLocation() {
        when(this.service.updatePrice(anyLong(), any(BigDecimal.class))).thenReturn(1L);
        StockUpdatePriceDTO mockStockUpdatePriceDTO = mock(StockUpdatePriceDTO.class);
        when(mockStockUpdatePriceDTO.getPrice()).thenReturn(BigDecimal.ONE);

        ResponseEntity<Void> response = this.controller.updatePrice(1L, mockStockUpdatePriceDTO);

        verify(this.service, times(1)).updatePrice(anyLong(), any(BigDecimal.class));
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        assertNotNull(response.getHeaders().getLocation());
    }
}