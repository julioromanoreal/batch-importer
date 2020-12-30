package com.julioromano.batchimporter.processing.sales;

public class SalesOutputData {
    private final int numberOfCustomers;
    private final int numberOfSalesman;
    private final String mostExpensiveSale;
    private final String worstSalesman;

    public SalesOutputData(int numberOfCustomers, int numberOfSalesman, String mostExpensiveSale, String worstSalesman) {
        this.numberOfCustomers = numberOfCustomers;
        this.numberOfSalesman = numberOfSalesman;
        this.mostExpensiveSale = mostExpensiveSale;
        this.worstSalesman = worstSalesman;
    }

    public int getNumberOfCustomers() {
        return numberOfCustomers;
    }

    public int getNumberOfSalesman() {
        return numberOfSalesman;
    }

    public String getMostExpensiveSale() {
        return mostExpensiveSale;
    }

    public String getWorstSalesman() {
        return worstSalesman;
    }
}
