package com.julioromano.batchimporter.processing.sales.pojo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Sale extends SaleData {

    private String id;
    private List<SaleItem> items = new ArrayList<>();
    private String salesmanName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<SaleItem> getItems() {
        return items;
    }

    public void setItems(List<SaleItem> items) {
        this.items = items;
    }

    public String getSalesmanName() {
        return salesmanName;
    }

    public void setSalesmanName(String salesmanName) {
        this.salesmanName = salesmanName;
    }
}
