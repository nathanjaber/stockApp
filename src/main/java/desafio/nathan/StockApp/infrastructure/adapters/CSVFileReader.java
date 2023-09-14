package desafio.nathan.StockApp.infrastructure.adapters;

import com.opencsv.CSVReader;
import desafio.nathan.StockApp.application.DTOs.StockCreationDTO;
import desafio.nathan.StockApp.ports.IStockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CSVFileReader {

    private final IStockService stockService;
    private final CSVReaderBuilderFactory csvReaderBuilderFactory;

    @Autowired
    public CSVFileReader(IStockService stockService, CSVReaderBuilderFactory csvReaderBuilderFactory) {
        this.stockService = stockService;
        this.csvReaderBuilderFactory = csvReaderBuilderFactory;
    }

    public void read(FileReader fileReader) {
        log.info("starting csv file reading");
        try {
            CSVReader reader = this.csvReaderBuilderFactory.createCSVReaderBuilder(fileReader).withSkipLines(1).build();

            List<StockCreationDTO> stockCreationDTOList = parseCSV(reader);
            stockCreationDTOList.forEach(this.stockService::create);
        } catch (Exception e) {
            log.error("error while processing CSV file", e);
            throw new RuntimeException(e);
        }
    }

    private List<StockCreationDTO> parseCSV(CSVReader reader) throws Exception {
        log.info("starting csv file conversion to list of StockDTO");
        List<StockCreationDTO> stockCreationDTOList =  reader.readAll().stream()
                .map(this::createDTOFromData)
                .collect(Collectors.toList());
        log.info("file converted successfully to list of StockDTO");
        return stockCreationDTOList;
    }

    private StockCreationDTO createDTOFromData(String[] data) {
        return new StockCreationDTO(data[0],
                LocalDate.parse(data[1], DateTimeFormatter.ofPattern("MMM d yyyy")),
                new BigDecimal(data[2]));
    }
}
