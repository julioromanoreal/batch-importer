package com.julioromano.batchimporter.processing;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface BatchProcessing {

    void process(Path dir, List<Path> files) throws IOException, InterruptedException, ExecutionException;

    void produceOutput(int customersQty, int salesmanQty, String mostExpensiveSale, String worstSalesman) throws IOException;
}
