package desafio.nathan.StockApp.infrastructure.adapters;

import com.opencsv.CSVReaderBuilder;
import org.springframework.stereotype.Component;

import java.io.FileReader;

@Component
public class CSVReaderBuilderFactory {

    public CSVReaderBuilder createCSVReaderBuilder(FileReader fileReader) {
        return new CSVReaderBuilder(fileReader);
    }
}
