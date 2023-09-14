package desafio.nathan.StockApp.application;

import desafio.nathan.StockApp.application.DTOs.StockCreationDTO;
import desafio.nathan.StockApp.application.DTOs.StockDTO;
import desafio.nathan.StockApp.application.exceptions.InternalServerException;
import desafio.nathan.StockApp.application.exceptions.NotFoundException;
import desafio.nathan.StockApp.application.mappers.StockMapper;
import desafio.nathan.StockApp.entity.Stock;
import desafio.nathan.StockApp.infrastructure.repositories.StockRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {
    @Mock
    private StockMapper mapper;

    @Mock
    private StockRepository repository;

    @InjectMocks
    private StockService service;

    @Test
    public void whenCreate_thenReturnInteger() {

        Stock mockStock = mock(Stock.class);
        when(this.mapper.toStock(any(StockCreationDTO.class))).thenReturn(mock(Stock.class));
        when(this.repository.save(any(Stock.class))).thenReturn(mockStock);
        when(mockStock.getId()).thenReturn(1L);
        try {
            this.service.create(mock(StockCreationDTO.class));
        } catch(Exception e) {
            fail(e);
        }

        verify(this.mapper, times(1)).toStock(any(StockCreationDTO.class));
        verify(this.repository, times(1)).save(any(Stock.class));
    }

    @Test
    public void givenSavingFailure_whenCreate_thenThrowDatabaseException() {

        Stock mockStock = mock(Stock.class);
        when(this.mapper.toStock(any(StockCreationDTO.class))).thenReturn(mock(Stock.class));
        when(this.repository.save(any(Stock.class))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(InternalServerException.class, () -> this.service.create(mock(StockCreationDTO.class)));

        verify(this.mapper, times(1)).toStock(any(StockCreationDTO.class));
        verify(this.repository, times(1)).save(any(Stock.class));
    }

    @Test
    public void whenUpdatePrice_thenReturnInt() {
        Stock mockStock = mock(Stock.class);
        when(this.repository.findById(anyLong())).thenReturn(Optional.ofNullable(mockStock));
        when(this.repository.save(any(Stock.class))).thenReturn(mockStock);
        doNothing().when(mockStock).setPrice(any(BigDecimal.class));
        when(mockStock.getId()).thenReturn(1L);

        try {
            this.service.updatePrice(1L, BigDecimal.ONE);
        } catch(Exception e) {
            fail(e);
        }

        verify(this.repository, times(1)).findById(anyLong());
        verify(this.repository, times(1)).save(any(Stock.class));
    }

    @Test
    public void whenGetAll_thenReturnListOfStockDTO() {
        when(this.repository.findAll()).thenReturn(List.of(mock(Stock.class)));
        when(this.mapper.toStockDTOList(anyList())).thenReturn(List.of(mock(StockDTO.class)));

        try {
            this.service.getAll();
        } catch(Exception e) {
            fail(e);
        }

        verify(this.repository, times(1)).findAll();
        verify(this.mapper, times(1)).toStockDTOList(anyList());
    }

    @Test
    public void givenExistentRecord_whenGetById_thenReturnOneStock() {
        when(this.repository.findById(anyLong())).thenReturn(Optional.of(mock(Stock.class)));
        when(this.mapper.toStockDTO(any(Stock.class))).thenReturn(mock(StockDTO.class));

        try {
            this.service.getById(1L);
        } catch(Exception e) {
            fail(e);
        }

        verify(this.repository, times(1)).findById(anyLong());
        verify(this.mapper, times(1)).toStockDTO(any(Stock.class));
    }


    @Test
    public void givenNonExistentRecord_whenGetById_thenThrowNotFoundException() {
        when(this.repository.findById(anyLong())).thenReturn(Optional.empty());


        assertThrows(NotFoundException.class, () -> this.service.getById(1L));

        verify(this.repository, times(1)).findById(anyLong());
        verify(this.mapper, times(0)).toStockDTO(any());
    }

    @Test
    public void givenNonExistentRecord_whenUpdatePrice_thenThrowNotFoundException() {
        when(this.repository.findById(anyLong())).thenReturn(Optional.empty());


        assertThrows(NotFoundException.class, () -> this.service.updatePrice(1L, BigDecimal.ONE));
        verify(this.repository, times(1)).findById(anyLong());
        verify(this.repository, times(0)).save(any(Stock.class));
    }

}