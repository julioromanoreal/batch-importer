package com.julioromano.batchimporter.batch.files;

import com.julioromano.batchimporter.batch.BatchWatcher;
import com.julioromano.batchimporter.batch.files.utils.FileUtils;
import com.julioromano.batchimporter.exceptions.FileWatchingException;
import com.julioromano.batchimporter.processing.BatchProcessing;
import com.julioromano.batchimporter.processing.BatchProcessingFactory;
import com.julioromano.batchimporter.exceptions.ProcessingException;
import com.julioromano.batchimporter.utils.AppProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

public class FileBatchWatcher implements BatchWatcher {

    private final static Logger LOGGER = LoggerFactory.getLogger(FileBatchWatcher.class);
    private static boolean EXECUTING = false;

    public void watchFiles(String path) throws FileWatchingException {
        LOGGER.info("Watching for new files in " + path);

        try {
            WatchService watcher = FileSystems.getDefault().newWatchService();
            WatchKey key = Path.of(path).register(watcher, ENTRY_CREATE);

            // Keep watching the directory waiting for new files to be processed
            // As soon as a new file gets created/copied into this directory, the process will be triggered
            for (; ; ) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    if (kind == OVERFLOW) {
                        continue;
                    }

                    // Only starts a new execution if the latest one is already finished
                    if (EXECUTING) {
                        continue;
                    }

                    execute(path);
                }

                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }
        } catch (IOException x) {
            LOGGER.error("Error watching for new files in " + path, x);
            throw new FileWatchingException(x);
        }

    }

    private void execute(String path) {
        EXECUTING = true;

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        long delay = Long.parseLong(AppProperties.getInstance().getProperty(AppProperties.TIME_TO_START_PROCESS)); // Time for more files to be copied into the folder
        scheduler.schedule(getTask(path), delay, TimeUnit.SECONDS);
        scheduler.shutdown();
    }

    private Runnable getTask(String path) {
        return () -> {
            try {
                List<Path> files = getFiles(path);
                BatchProcessing salesBatchProcessing = BatchProcessingFactory.getSalesBatchProcessing();
                salesBatchProcessing.process(files);
            } catch (IOException | ProcessingException e) {
                LOGGER.error("Error processing files", e);
            } finally {
                EXECUTING = false;
            }
        };
    }

    private List<Path> getFiles(String path) throws IOException {
        return Files.walk(Paths.get(path))
                .filter(Files::isRegularFile)
                .filter(f -> {
                    Optional<String> extension = FileUtils.getExtensionByStringHandling(f.toString());
                    return extension.isPresent() && extension.get().equals("dat");
                })
                .collect(Collectors.toList());
    }

}
