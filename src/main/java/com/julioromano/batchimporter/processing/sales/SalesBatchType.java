package com.julioromano.batchimporter.processing.sales;

public enum SalesBatchType {

    SALESMAN("001", "([0-9]+)ç([0-9]+)ç(.+)ç([0-9\\.]+)"),
    CUSTOMER("002", "([0-9]+)ç([0-9]+)ç(.+)ç(.+)"),
    SALE("003", "([0-9]+)ç([0-9]+)ç(.+)ç(.+)");

    private final String identifier;
    private final String regexSplitter;

    SalesBatchType(String identifier, String regexSplitter) {
        this.identifier = identifier;
        this.regexSplitter = regexSplitter;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getRegexSplitter() {
        return regexSplitter;
    }
}
