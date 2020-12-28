package com.julioromano.batchimporter;

import com.julioromano.batchimporter.batch.BatchWatcher;
import com.julioromano.batchimporter.batch.BatchWatcherFactory;
import com.julioromano.batchimporter.utils.AppProperties;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        BatchWatcher fileBatchWatcher = BatchWatcherFactory.getFileBatchWatcher();

        String salesDataInDir = AppProperties.getInstance().getProperty(AppProperties.SALES_DATA_IN_DIR);
        fileBatchWatcher.watchFiles(salesDataInDir);

        Scanner scanner = new Scanner(System.in);
        for (; ; ) { // In order to keep the program running indefinitely
            scanner.nextLine();
        }
    }

}
