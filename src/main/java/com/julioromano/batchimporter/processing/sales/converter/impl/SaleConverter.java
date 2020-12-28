package com.julioromano.batchimporter.processing.sales.converter.impl;

import com.julioromano.batchimporter.processing.sales.converter.SalesDataConverter;
import com.julioromano.batchimporter.processing.sales.pojo.Sale;
import com.julioromano.batchimporter.processing.sales.pojo.SaleItem;

import java.math.BigDecimal;

public class SaleConverter implements SalesDataConverter<Sale> {

    @Override
    public Sale convert(String[] parts) {
        Sale sale = new Sale();
        sale.setId(parts[1]);

        String[] saleItems = parts[2].substring(1, parts[2].length() - 1).split(",");
        for (String saleItemStr : saleItems) {
            String[] saleItemParts = saleItemStr.split("-");

            SaleItem saleItem = new SaleItem();
            saleItem.setItemId(Long.valueOf(saleItemParts[0]));
            saleItem.setQuantity(Long.valueOf(saleItemParts[1]));
            saleItem.setPrice(new BigDecimal(saleItemParts[2]));

            sale.getItems().add(saleItem);
        }

        sale.setSalesmanName(parts[3]);

        return sale;
    }
}
