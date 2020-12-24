package com.julioromano.batchimporter;

import com.julioromano.batchimporter.batch.BatchWatcher;
import com.julioromano.batchimporter.batch.BatchWatcherFactory;
import com.julioromano.batchimporter.utils.AppProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    private final static Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        BatchWatcher fileBatchWatcher = BatchWatcherFactory.getFileBatchWatcher();
        try {
            String dataInDir = AppProperties.getInstance().getProperty(AppProperties.DATA_IN_DIR);
            fileBatchWatcher.watchFiles(dataInDir);
        } catch (IOException e) {
            LOGGER.error("Error getting File Watcher", e);
        }

        Scanner scanner = new Scanner(System.in);
        for (;;) {
            scanner.nextLine();
        }
    }

}
