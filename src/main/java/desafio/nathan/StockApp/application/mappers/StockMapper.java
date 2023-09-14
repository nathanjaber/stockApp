package desafio.nathan.StockApp.application.mappers;

import desafio.nathan.StockApp.application.DTOs.StockCreationDTO;
import desafio.nathan.StockApp.application.DTOs.StockDTO;
import desafio.nathan.StockApp.entity.Stock;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StockMapper {
    public Stock toStock(StockCreationDTO stockCreationDTO) {
        return new Stock(stockCreationDTO.getSymbol(),
                stockCreationDTO.getDate(),
                stockCreationDTO.getPrice());
    }

    public StockDTO toStockDTO(Stock stock) {
        return new StockDTO(stock.getId(),
                stock.getSymbol(),
                stock.getDate(),
                stock.getPrice());
    }

    public List<StockDTO> toStockDTOList(List<Stock> stockList) {
        List<StockDTO> stockDTOList = new ArrayList<>();
        if (!stockList.isEmpty()) {
            for (Stock stock : stockList) {
                stockDTOList.add(this.toStockDTO(stock));
            }
        }
        return stockDTOList;
    }
}
