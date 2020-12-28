package com.julioromano.batchimporter.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppProperties {

    public static final String FILE_DELIMITER = "fileDelimiter";
    public static final String TIME_TO_START_PROCESS = "timeToStartProcess";
    public static final String SALES_DATA_IN_DIR = "salesDataInDir";
    public static final String SALES_DATA_OUT_DIR = "salesDataOutDir";
    private static AppProperties INSTANCE;
    private Properties properties;

    private AppProperties() {
        try (InputStream inputStream = getClass()
                .getClassLoader().getResourceAsStream("application.properties")) {

            properties = new Properties();
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized AppProperties getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AppProperties();
        }

        return INSTANCE;
    }

    public String getProperty(String property) {
        return properties.getProperty(property);
    }
}
