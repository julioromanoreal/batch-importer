package com.julioromano.batchimporter.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppProperties {

    private static AppProperties INSTANCE;
    private Properties properties;

    public static final String FILE_DELIMITER = "fileDelimiter";
    public static final String DATA_IN_DIR = "dataInDir";
    public static final String DATA_OUT_DIR = "dataOutDir";

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
