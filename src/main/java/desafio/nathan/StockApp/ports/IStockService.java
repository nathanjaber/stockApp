package desafio.nathan.StockApp.ports;

import desafio.nathan.StockApp.application.DTOs.StockCreationDTO;
import desafio.nathan.StockApp.application.DTOs.StockDTO;

import java.math.BigDecimal;
import java.util.List;

public interface IStockService {
    Long create(StockCreationDTO stockCreationDTO);
    Long updatePrice(Long id, BigDecimal price);
    List<StockDTO> getAll();
    StockDTO getById(Long id);

}
