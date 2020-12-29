package com.julioromano.batchimporter.processing.sales.pojo;

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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long cnpj;
        private String name;
        private String businessArea;

        public Builder cnpj(Long cnpj) {
            this.cnpj = cnpj;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder businessArea(String businessArea) {
            this.businessArea = businessArea;
            return this;
        }

        public Customer build() {
            Customer customer = new Customer();
            customer.setCnpj(this.cnpj);
            customer.setName(this.name);
            customer.setBusinessArea(this.businessArea);
            return customer;
        }
    }
}
