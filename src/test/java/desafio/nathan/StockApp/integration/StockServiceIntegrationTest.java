package desafio.nathan.StockApp.integration;


import desafio.nathan.StockApp.application.DTOs.StockCreationDTO;
import desafio.nathan.StockApp.application.DTOs.StockDTO;
import desafio.nathan.StockApp.application.StockService;
import desafio.nathan.StockApp.application.exceptions.InternalServerException;
import desafio.nathan.StockApp.entity.Stock;
import desafio.nathan.StockApp.infrastructure.repositories.StockRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles(profiles = {"test"})
public class StockServiceIntegrationTest {

    @Autowired
    private StockService stockService;

    @Autowired
    private StockRepository stockRepository;

    @Test
    public void whenCreateWithValidStockDTO_thenReturnsNewRecordsId() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy");
        LocalDate originalDate = LocalDate.of(2023, 9, 11);
        String formattedDate = originalDate.format(formatter);
        LocalDate date = LocalDate.parse(formattedDate, formatter);

        StockCreationDTO stockCreationDTO = new StockCreationDTO("TEST", date, BigDecimal.ONE);

        Long id = this.stockService.create(stockCreationDTO);

        assertNotNull(id);
        Optional<Stock> optionalStock = this.stockRepository.findById(id);
        assertTrue(optionalStock.isPresent());
        Stock createdStock = optionalStock.get();
        assertEquals(stockCreationDTO.getSymbol() ,createdStock.getSymbol());
        assertEquals(stockCreationDTO.getDate() ,createdStock.getDate());
        assertEquals(stockCreationDTO.getPrice() ,createdStock.getPrice());
    }


    @Test
    public void whenCreateWithStockDTOWithNullValues_thenThowsInternalServerException() {

        StockCreationDTO stockCreationDTO = new StockCreationDTO(null,null, null);

        assertThrows(InternalServerException.class, () -> this.stockService.create(stockCreationDTO) );
    }

    @Test
    public void givenExistentRecordAndValidPrice_whenUpdatePrice_thenReturnsUpdatedRecordsId() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy");
        LocalDate originalDate = LocalDate.of(2023, 9, 11);
        String formattedDate = originalDate.format(formatter);
        LocalDate date = LocalDate.parse(formattedDate, formatter);

        Stock stockToSave = new Stock("TEST", date, BigDecimal.ONE);
        Stock createdStock = this.stockRepository.save(stockToSave);

        BigDecimal newPrice = new BigDecimal("123.45");
        Long updatedId = this.stockService.updatePrice(createdStock.getId(), newPrice);

        assertEquals(createdStock.getId(), updatedId);
        Stock updatedStock = this.stockRepository.findById(updatedId).orElse(null);
        assertNotNull(updatedStock);
        assertEquals(newPrice, updatedStock.getPrice());
    }

    @Test
    public void whenGetAll_thenReturnsListOfRecords() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy");
        LocalDate originalDate = LocalDate.of(2023, 9, 11);
        String formattedDate = originalDate.format(formatter);
        LocalDate date = LocalDate.parse(formattedDate, formatter);

        this.stockRepository.save(new Stock("TEST", date, BigDecimal.ONE));
        this.stockRepository.save(new Stock("TEST", date, BigDecimal.ONE));
        this.stockRepository.save(new Stock("TEST", date, BigDecimal.ONE));

        List<StockDTO> stockDTOList = this.stockService.getAll();
        assertFalse(stockDTOList.isEmpty());
    }

    @Test
    public void givenExistentRecord_whenGetById_thenReturnsRecord() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy");
        LocalDate originalDate = LocalDate.of(2023, 9, 11);
        String formattedDate = originalDate.format(formatter);
        LocalDate date = LocalDate.parse(formattedDate, formatter);

        Stock stock = this.stockRepository.save(new Stock("TEST", date, BigDecimal.ONE));

        StockDTO stockDTO = this.stockService.getById(stock.getId());
        assertNotNull(stockDTO);
        assertEquals(stock.getId(), stockDTO.getId());
    }

}

