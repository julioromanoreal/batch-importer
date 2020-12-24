package com.julioromano.batchimporter.processing.sales.converter.impl;

import com.julioromano.batchimporter.processing.sales.converter.SalesDataConverter;
import com.julioromano.batchimporter.processing.sales.pojo.Customer;

public class CustomerConverter implements SalesDataConverter<Customer> {

    @Override
    public Customer convert(String[] parts) {
        Customer customer = new Customer();
        customer.setCnpj(Long.valueOf(parts[1]));
        customer.setName(parts[2]);
        customer.setBusinessArea(parts[3]);

        return customer;
    }
}
