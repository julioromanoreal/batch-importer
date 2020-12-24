package com.julioromano.batchimporter.processing.sales.converter;

import com.julioromano.batchimporter.processing.sales.pojo.SaleData;

public interface SalesDataConverter<E extends SaleData> {

    E convert(String[] parts);

}
