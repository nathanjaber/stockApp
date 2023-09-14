package desafio.nathan.StockApp.application;

import desafio.nathan.StockApp.application.DTOs.StockCreationDTO;
import desafio.nathan.StockApp.application.DTOs.StockDTO;
import desafio.nathan.StockApp.application.exceptions.InternalServerException;
import desafio.nathan.StockApp.application.exceptions.NotFoundException;
import desafio.nathan.StockApp.application.mappers.StockMapper;
import desafio.nathan.StockApp.entity.Stock;
import desafio.nathan.StockApp.infrastructure.repositories.StockRepository;
import desafio.nathan.StockApp.ports.IStockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class StockService implements IStockService {

    @Autowired
    private StockMapper mapper;
    @Autowired
    private StockRepository repository;

    @Override
    public Long create(StockCreationDTO stockCreationDTO) {
        log.info("starting stock creation");
        try {
            Stock stock = this.mapper.toStock(stockCreationDTO);
            log.info("creating stock ({}, {})", stock.getSymbol(), stock.getDate());
            Long createdStockId = this.repository.save(stock).getId();
            log.info("stock created, id {}", createdStockId);
            return createdStockId;
        } catch (Exception e) {
            log.error("something went wrong while creating stock ({}, {})", stockCreationDTO.getSymbol(), stockCreationDTO.getDate());
            throw new InternalServerException("Error while creating stock", e.getCause());
        }
    }

    @Override
    public Long updatePrice(Long id, BigDecimal price) {
        log.info("starting stock`s price update");
        try {
            log.info("trying to find stock with id {}", id);
            Optional<Stock> optionalStock = this.repository.findById(id);
            if (optionalStock.isPresent()) {
                log.info("found stock with id {}", id);
                Stock stock = optionalStock.get();
                stock.setPrice(price);

                log.info("updating stock`s price with id {}", id);
                Stock updatedStock = this.repository.save(stock);
                log.info("stock`s price with id {} updated", id);
                return updatedStock.getId();
            } else {
                throw new NotFoundException("stock not found");
            }
        } catch (NotFoundException e) {
            log.warn("stock with id {} not found", id);
            throw e;
        } catch (Exception e) {
            log.error("something went wrong while updating stock {} price", id);
            throw new InternalServerException("Error while updating stock`s price", e.getCause());
        }
    }

    @Override
    public List<StockDTO> getAll() {
        log.info("starting stocks listing");
        try {
            List<Stock> stockList = this.repository.findAll();
            log.info("stocks found");
            return this.mapper.toStockDTOList(stockList);
        } catch (Exception e) {
            log.error("something went wrong while listing stocks");
            throw new InternalServerException("Error while listing stocks", e.getCause());
        }
    }

    @Override
    public StockDTO getById(Long id) {
        try {
            log.info("trying to find stock with id {}", id);
            Optional<Stock> optionalStock = this.repository.findById(id);
            if (optionalStock.isPresent()) {
                log.info("stock with id {} found ", id);
                return this.mapper.toStockDTO(optionalStock.get());
            }
            throw new NotFoundException("stock not found");
        } catch (NotFoundException e) {
            log.warn("stock with id {} not found", id);
            throw e;
        } catch (Exception e) {
            log.error("something went wrong while finding stock with id {}", id);
            throw new InternalServerException("Error while finding stock", e.getCause());
        }
    }
}
