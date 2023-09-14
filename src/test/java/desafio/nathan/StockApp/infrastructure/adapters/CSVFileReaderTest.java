package desafio.nathan.StockApp.infrastructure.adapters;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import desafio.nathan.StockApp.ports.IStockService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class CSVFileReaderTest {

    @Mock
    private IStockService stockService;

    @Mock
    private CSVReaderBuilderFactory csvReaderBuilderFactory;

    @Mock
    private CSVReaderBuilder csvReaderBuilder;

    @Mock
    private CSVReader csvReader;

    @InjectMocks
    private CSVFileReader csvFileReader;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(csvReaderBuilderFactory.createCSVReaderBuilder(any())).thenReturn(csvReaderBuilder);
        when(csvReaderBuilder.withSkipLines(anyInt())).thenReturn(csvReaderBuilder);
        when(csvReaderBuilder.build()).thenReturn(csvReader);
    }


}
