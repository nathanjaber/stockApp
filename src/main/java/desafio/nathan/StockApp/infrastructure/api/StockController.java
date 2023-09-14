package desafio.nathan.StockApp.infrastructure.api;

import desafio.nathan.StockApp.application.DTOs.StockCreationDTO;
import desafio.nathan.StockApp.application.DTOs.StockDTO;
import desafio.nathan.StockApp.application.DTOs.StockUpdatePriceDTO;
import desafio.nathan.StockApp.ports.IStockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.http.ResponseEntity.*;

;

@Slf4j
@RestController
@RequestMapping(path="stocks", produces = MediaType.APPLICATION_JSON_VALUE)
public class StockController {

    @Autowired
    private IStockService service;

    @GetMapping("/{id}")
    public ResponseEntity<StockDTO> get(@PathVariable Long id) {
        log.info("teste");
        return ok(this.service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<StockDTO>> getAll() {
        List<StockDTO> stockDTOList = this.service.getAll();
        return ok(stockDTOList);
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid StockCreationDTO stockCreationDTO) {
        Long stockId = this.service.create(stockCreationDTO);
        return created(this.generateLocationHeader(stockId)).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePrice(@PathVariable Long id,
                                            @RequestBody @Valid StockUpdatePriceDTO stockUpdatePriceDTO) {
        Long stockId = this.service.updatePrice(id, stockUpdatePriceDTO.getPrice());
        return noContent().location(this.generateLocationHeader(stockId)).build();
    }


    private URI generateLocationHeader(Long stockId) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(stockId)
                .toUri();
    }


}
