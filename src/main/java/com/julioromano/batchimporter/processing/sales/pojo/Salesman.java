package com.julioromano.batchimporter.processing.sales.pojo;

import java.math.BigDecimal;

public class Salesman extends SaleData {

    private Long cpf;
    private String name;
    private BigDecimal salary;

    public Long getCpf() {
        return cpf;
    }

    public void setCpf(Long cpf) {
        this.cpf = cpf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long cpf;
        private String name;
        private BigDecimal salary;

        public Builder cpf(Long cpf) {
            this.cpf = cpf;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder salary(BigDecimal salary) {
            this.salary = salary;
            return this;
        }

        public Salesman build() {
            Salesman salesman = new Salesman();
            salesman.setCpf(this.cpf);
            salesman.setName(this.name);
            salesman.setSalary(this.salary);
            return salesman;
        }
    }
}
