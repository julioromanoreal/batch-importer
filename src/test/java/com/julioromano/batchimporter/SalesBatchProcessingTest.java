package com.julioromano.batchimporter;

import com.julioromano.batchimporter.processing.sales.SalesBatchProcessing;
import org.apache.commons.io.LineIterator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SalesBatchProcessingTest {

    @Test
    public void givenFileContentReturnCorrectResult() {
        StringReader reader = new StringReader("001ç1234567891234çPedroç50000\n" +
                "001ç3245678865434çPauloç40000.99\n" +
                "002ç2345675434544345çJose da SilvaçRural\n" +
                "002ç2345675433444345çEduardo PereiraçRural\n" +
                "003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro\n" +
                "003ç08ç[1-34-10,2-33-1.50,3-40-0.10]çPaulo\n");
        LineIterator it = new LineIterator(reader);

        SalesBatchProcessing tester = new SalesBatchProcessing();
        SalesBatchProcessing.DataResult result = tester.processFile("ç", it);

        assertEquals(result.getCustomersQty(), 2);
        assertEquals(result.getSalesmanQty(), 2);
        assertEquals(result.getMostExpensiveSale(), "10");
        assertEquals(result.getSalesBySalesman().get("Paulo").setScale(2, RoundingMode.HALF_UP), new BigDecimal("11.60"));
        assertEquals(result.getSalesBySalesman().get("Pedro").setScale(2, RoundingMode.HALF_UP), new BigDecimal("105.60"));
    }
}
