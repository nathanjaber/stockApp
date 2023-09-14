package desafio.nathan.StockApp.application.mappers;

import desafio.nathan.StockApp.application.DTOs.StockCreationDTO;
import desafio.nathan.StockApp.application.DTOs.StockDTO;
import desafio.nathan.StockApp.entity.Stock;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StockMapperTest {

    private StockMapper mapper;

    public StockMapperTest() {
        this.mapper = new StockMapper();
    }

    @Test
    public void whenToStockWithStockCreationDTO_thenReturnsStock() {
        String symbolMock = "ABCD";
        LocalDate dateMock = LocalDate.now();
        BigDecimal priceMock = BigDecimal.ONE;

        StockCreationDTO mockStockCreationDTO = mock(StockCreationDTO.class);
        when(mockStockCreationDTO.getSymbol()).thenReturn(symbolMock);
        when(mockStockCreationDTO.getDate()).thenReturn(dateMock);
        when(mockStockCreationDTO.getPrice()).thenReturn(priceMock);

        Stock stock = this.mapper.toStock(mockStockCreationDTO);
        assertEquals(symbolMock, stock.getSymbol());
        assertEquals(dateMock, stock.getDate());
        assertEquals(priceMock, stock.getPrice());
    }


    @Test
    public void whenToStockWithStockDTO_thenReturnsStock() {
        String symbolMock = "ABCD";
        LocalDate dateMock = LocalDate.now();
        BigDecimal priceMock = BigDecimal.ONE;

        Stock mockStock = mock(Stock.class);
        when(mockStock.getSymbol()).thenReturn(symbolMock);
        when(mockStock.getDate()).thenReturn(dateMock);
        when(mockStock.getPrice()).thenReturn(priceMock);

        StockDTO stockDTO = this.mapper.toStockDTO(mockStock);


        assertNotNull(stockDTO.getId());
        assertEquals(symbolMock, stockDTO.getSymbol());
        assertEquals(dateMock, stockDTO.getDate());
        assertEquals(priceMock, stockDTO.getPrice());
    }

    @Test
    public void whenToStockWithStockList_thenReturnsStock() {
        Stock mockStock = mock(Stock.class);

        List<Stock> mockStockList = List.of(mockStock);

        List<StockDTO> stockDTOList = this.mapper.toStockDTOList(mockStockList);
        assertEquals(1, stockDTOList.size());
    }


}