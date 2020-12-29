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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String id;
        private final List<SaleItem> items = new ArrayList<>();
        private String salesmanName;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder salesmanName(String salesmanName) {
            this.salesmanName = salesmanName;
            return this;
        }

        public Builder items(String itemsString) {
            String[] saleItems = itemsString.substring(1, itemsString.length() - 1).split(",");
            for (String saleItemStr : saleItems) {
                String[] saleItemParts = saleItemStr.split("-");

                SaleItem saleItem = new SaleItem();
                saleItem.setItemId(Long.valueOf(saleItemParts[0]));
                saleItem.setQuantity(Long.valueOf(saleItemParts[1]));
                saleItem.setPrice(new BigDecimal(saleItemParts[2]));

                items.add(saleItem);
            }

            return this;
        }

        public Sale build() {
            Sale sale = new Sale();
            sale.setId(this.id);
            sale.setSalesmanName(this.salesmanName);
            sale.setItems(this.items);
            return sale;
        }
    }
}
