package com.julioromano.batchimporter.processing.sales;

public enum SalesBatchType {

    SALESMAN("001", "([0-9]+)DEL([0-9]+)DEL(.+)DEL([0-9\\.]+)"),
    CUSTOMER("002", "([0-9]+)DEL([0-9]+)DEL(.+)DEL(.+)"),
    SALE("003", "([0-9]+)DEL([0-9]+)DEL(.+)DEL(.+)");

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
