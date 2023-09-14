package desafio.nathan.StockApp.infrastructure.adapters;


import desafio.nathan.StockApp.infrastructure.WatchServiceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

@Slf4j
@Component
public final class FileMonitor implements ApplicationRunner {

    private final CSVFileReader csvFileReader;
    private final WatchServiceFactory watchServiceFactory;
    private final String directory;
    private final String filename;
    private final String sysPath;
    private volatile boolean isRunning = true;

    @Autowired
    public FileMonitor(CSVFileReader csvFileReader,
                       WatchServiceFactory watchServiceFactory,
                       @Value("${file-reader.dir-path:directory}") String directory,
                       @Value("${file-reader.system-path:sysPath}") String sysPath,
                       @Value("${file-reader.filename:filename}") String filename) {
        this.csvFileReader = csvFileReader;
        this.watchServiceFactory = watchServiceFactory;
        this.directory = directory;
        this.sysPath = sysPath;
        this.filename = filename;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
       this.monitor();
    }

    public void stopMonitoring() {
        this.isRunning = false;
    }

    private void monitor() {
        log.info("Starting the file monitor service");

        String directory = System.getProperty(this.sysPath);

        String fullPath = directory + this.directory;

        try {
            WatchService watchService = watchServiceFactory.createWatchService();
            Path directoryPath = Paths.get(fullPath);
            directoryPath.register(watchService, ENTRY_CREATE);

            log.info("Monitoring directory: {}", directoryPath);

            new Thread(() -> {
                try {
                    while (isRunning) {
                        this.handleWatchEvents(watchService);
                    }
                } catch (InterruptedException e) {
                    log.warn("File monitor service interrupted");
                    throw new RuntimeException(e);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        log.info("finishing the file monitor service");
                        watchService.close();
                        log.info("finished the file monitor service");
                    } catch (IOException e) {
                        log.error("error finishing the file monitor service");
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void handleWatchEvents(WatchService watchService) throws InterruptedException, FileNotFoundException {
        WatchKey key = watchService.take();
        Thread.sleep(3000);
        for (WatchEvent<?> event : key.pollEvents()) {
            processEvent(Paths.get(directory), event);
        }
        key.reset();
    }

    private void processEvent(Path directoryPath, WatchEvent<?> event) throws FileNotFoundException {
        Path file = directoryPath.resolve((Path) event.context());
        String name = file.getFileName().toString();

        log.info("File changed: {}", name);
        log.info("filename {}", this.filename);

        if (event.kind() == ENTRY_CREATE && name.equals(this.filename)) {
            try (FileReader fileReader = new FileReader(this.directory.substring(1) + "/" + this.filename)) {
                this.csvFileReader.read(fileReader);
            } catch (IOException e) {
                log.error("csv file not found");
            }


        }
    }
}
