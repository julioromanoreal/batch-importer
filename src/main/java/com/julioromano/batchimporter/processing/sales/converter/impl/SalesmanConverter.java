package com.julioromano.batchimporter.processing.sales.converter.impl;

import com.julioromano.batchimporter.processing.sales.converter.SalesDataConverter;
import com.julioromano.batchimporter.processing.sales.pojo.Salesman;

import java.math.BigDecimal;

public class SalesmanConverter implements SalesDataConverter<Salesman> {

    @Override
    public Salesman convert(String[] parts) {
        Salesman salesman = new Salesman();
        salesman.setCpf(Long.valueOf(parts[1]));
        salesman.setName(parts[2]);
        salesman.setSalary(new BigDecimal(parts[3]));

        return salesman;
    }
}
