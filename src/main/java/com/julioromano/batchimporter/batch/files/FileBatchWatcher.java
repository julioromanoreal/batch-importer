package com.julioromano.batchimporter.batch.files;

import com.julioromano.batchimporter.batch.BatchWatcher;
import com.julioromano.batchimporter.batch.files.utils.FileUtils;
import com.julioromano.batchimporter.processing.BatchProcessing;
import com.julioromano.batchimporter.processing.BatchProcessingFactory;
import exceptions.ProcessingException;
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

    public void watchFiles(String path) throws ProcessingException {
        LOGGER.debug("Watching for new files in " + path);

        Path dir = Path.of(path);

        try {
            WatchService watcher = FileSystems.getDefault().newWatchService();
            WatchKey key = dir.register(watcher, ENTRY_CREATE);

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

                    EXECUTING = true;

                    Runnable task = () -> {
                        try {
                            List<Path> files = Files.walk(Paths.get(path))
                                    .filter(Files::isRegularFile)
                                    .filter(f -> {
                                        Optional<String> extension = FileUtils.getExtensionByStringHandling(f.toString());
                                        return extension.isPresent() && extension.get().equals("dat");
                                    })
                                    .collect(Collectors.toList());

                            BatchProcessing salesBatchProcessing = BatchProcessingFactory.getSalesBatchProcessing();
                            salesBatchProcessing.process(dir, files);
                        } catch (IOException | ProcessingException e) {
                            LOGGER.error("Error processing files", e);
                        } finally {
                            EXECUTING = false;
                        }
                    };

                    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
                    int delay = 1; // Time for more files to be copied into the folder
                    scheduler.schedule(task, delay, TimeUnit.SECONDS);
                    scheduler.shutdown();
                }

                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }
        } catch (IOException x) {
            LOGGER.error("Error watching for new files in " + path, x);
            throw new ProcessingException(x);
        }

    }

}
