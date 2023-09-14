package desafio.nathan.StockApp.infrastructure;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.WatchService;

@Component
public class WatchServiceFactory {

    public WatchService createWatchService() throws IOException {
        return FileSystems.getDefault().newWatchService();
    }
}

