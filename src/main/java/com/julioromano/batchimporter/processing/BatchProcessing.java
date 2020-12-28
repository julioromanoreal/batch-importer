package com.julioromano.batchimporter.processing;

import exceptions.ProcessingException;

import java.nio.file.Path;
import java.util.List;

public interface BatchProcessing {

    void process(Path dir, List<Path> files) throws ProcessingException;

    void produceOutput(int customersQty, int salesmanQty, String mostExpensiveSale, String worstSalesman) throws ProcessingException;
}
