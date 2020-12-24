package com.julioromano.batchimporter.processing;

import com.julioromano.batchimporter.processing.sales.SalesBatchProcessing;

public abstract class BatchProcessingFactory {

    public static BatchProcessing getSalesBatchProcessing() {
        return new SalesBatchProcessing();
    }

}
