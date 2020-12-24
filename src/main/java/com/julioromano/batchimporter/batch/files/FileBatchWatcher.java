package com.julioromano.batchimporter.batch.files;

import com.julioromano.batchimporter.batch.BatchWatcher;
import com.julioromano.batchimporter.batch.files.utils.FileUtils;
import com.julioromano.batchimporter.processing.BatchProcessing;
import com.julioromano.batchimporter.processing.BatchProcessingFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

public class FileBatchWatcher implements BatchWatcher {

    private final static Logger LOGGER = LoggerFactory.getLogger(FileBatchWatcher.class);
    private static boolean EXECUTING = false;

    public void watchFiles(String path) throws IOException {
        LOGGER.debug("Watching for new files in " + path);

        Path dir = Path.of(path);

        try {
            WatchService watcher = FileSystems.getDefault().newWatchService();
            WatchKey key = dir.register(watcher, ENTRY_CREATE);

            for (;;) {
                for (WatchEvent<?> event: key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    if (kind == OVERFLOW) {
                        continue;
                    }

                    if (EXECUTING) {
                        continue;
                    }

                    EXECUTING = true;

                    Runnable task = new Runnable() {
                        public void run() {
                            try {
                                List<Path> files = Files.walk(Paths.get(path))
                                        .filter(Files::isRegularFile)
                                        .filter(f -> {
                                            Optional<String> extension = FileUtils.getExtensionByStringHandling(f.toString());
                                            if (extension.isEmpty() || ! extension.get().equals("dat")) {
                                                return false;
                                            }

                                            return true;
                                        })
                                        .collect(Collectors.toList());

                                BatchProcessing salesBatchProcessing = BatchProcessingFactory.getSalesBatchProcessing();
                                salesBatchProcessing.process(dir, files);
                            } catch (IOException | InterruptedException | ExecutionException e) {
                                LOGGER.error("Error processing files", e);
                            } finally {
                                EXECUTING = false;
                            }
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
            throw x;
        }

    }

}
