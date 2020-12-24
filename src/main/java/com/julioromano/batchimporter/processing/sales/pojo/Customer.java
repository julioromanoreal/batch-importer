package com.julioromano.batchimporter.processing.sales.pojo;

import java.math.BigDecimal;

public class Customer extends SaleData {

    private Long cnpj;
    private String name;
    private String businessArea;

    public Long getCnpj() {
        return cnpj;
    }

    public void setCnpj(Long cnpj) {
        this.cnpj = cnpj;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBusinessArea() {
        return businessArea;
    }

    public void setBusinessArea(String businessArea) {
        this.businessArea = businessArea;
    }
}
