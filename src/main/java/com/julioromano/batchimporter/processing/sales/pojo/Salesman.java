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
}
