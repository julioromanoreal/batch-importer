package com.julioromano.batchimporter;

import com.julioromano.batchimporter.processing.sales.SalesBatchProcessing;
import com.julioromano.batchimporter.processing.sales.SalesOutputData;
import org.apache.commons.io.LineIterator;
import org.junit.Test;

import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SalesBatchProcessingTest {

    @Test
    public void givenCorrectFileContentThenCustomersShouldBeCorrectlyAnalyzed() {
        StringReader reader = new StringReader("""
                001ç1234567891234çPedroç50000
                001ç3245678865434çPauloç40000.99
                002ç2345675434544345çJose da SilvaçRural
                002ç2345675433444345çEduardo PereiraçRural
                003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro
                003ç08ç[1-34-10,2-33-1.50,3-40-0.10]çPaulo
                """);
        LineIterator it = new LineIterator(reader);
        SalesBatchProcessing tester = new SalesBatchProcessing();
        var result = tester.processFile("ç", it);
        assertEquals(result.getCustomersQty(), 2);
    }

    @Test
    public void givenCorrectFileContentThenSalesmansShouldBeCorrectlyAnalyzed() {
        StringReader reader = new StringReader("""
                001ç1234567891234çPedroç50000
                001ç3245678865434çPauloç40000.99
                002ç2345675434544345çJose da SilvaçRural
                002ç2345675433444345çEduardo PereiraçRural
                003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro
                003ç08ç[1-34-10,2-33-1.50,3-40-0.10]çPaulo
                """);
        LineIterator it = new LineIterator(reader);
        SalesBatchProcessing tester = new SalesBatchProcessing();
        var result = tester.processFile("ç", it);
        assertEquals(result.getSalesmanQty(), 2);
    }

    @Test
    public void givenCorrectFileContentThenMostExpensiveSaleShouldBeCorrectlyAnalyzed() {
        StringReader reader = new StringReader("""
                001ç1234567891234çPedroç50000
                001ç3245678865434çPauloç40000.99
                002ç2345675434544345çJose da SilvaçRural
                002ç2345675433444345çEduardo PereiraçRural
                003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro
                003ç08ç[1-34-10,2-33-1.50,3-40-0.10]çPaulo
                """);
        LineIterator it = new LineIterator(reader);
        SalesBatchProcessing tester = new SalesBatchProcessing();
        var result = tester.processFile("ç", it);
        assertEquals(result.getMostExpensiveSale(), "10");
    }

    @Test
    public void givenCorrectFileContentThenSalesFromASalesmanShouldBeCorrectlyAnalyzed() {
        StringReader reader = new StringReader("""
                001ç1234567891234çPedroç50000
                001ç3245678865434çPauloç40000.99
                002ç2345675434544345çJose da SilvaçRural
                002ç2345675433444345çEduardo PereiraçRural
                003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro
                003ç08ç[1-34-10,2-33-1.50,3-40-0.10]çPaulo
                """);
        LineIterator it = new LineIterator(reader);
        SalesBatchProcessing tester = new SalesBatchProcessing();
        var result = tester.processFile("ç", it);
        assertEquals(result.getSalesBySalesman().get("Paulo").setScale(2, RoundingMode.HALF_UP), new BigDecimal("393.50"));
    }

    @Test
    public void givenFileContentThenCorrectResultShouldBeReturned() {
        SalesBatchProcessing tester = new SalesBatchProcessing();
        String output = tester.getFormattedOutput(new SalesOutputData(2, 2, "10", "Paulo"));
        assertEquals(output, """
                Clientes: 2
                Vendedores: 2
                Venda mais cara: 10
                Pior vendedor: Paulo
                """);
    }

    @Test
    public void givenSalesmanWithNoSalesThenMapShouldHaveTheirNameWithZero() {
        StringReader reader = new StringReader("""
                001ç1234567891234çPedroç50000
                001ç3245678865434çPauloç40000.99
                001ç9745670165474çJoãoç40000.98
                002ç2345675434544345çJose da SilvaçRural
                002ç2345675433444345çEduardo PereiraçRural
                003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro
                003ç08ç[1-34-10,2-33-1.50,3-40-0.10]çPaulo
                """);
        LineIterator it = new LineIterator(reader);
        SalesBatchProcessing tester = new SalesBatchProcessing();
        var result = tester.processFile("ç", it);
        assertEquals(result.getSalesBySalesman().get("João"), BigDecimal.ZERO);
    }

    @Test
    public void givenSalesmanNameContainingTheFileDelimiterThenItShouldNotBeTruncated() {
        StringReader reader = new StringReader("""
                001ç1234567891234çPedroç50000
                001ç3245678865434çPauloç40000.99
                001ç9745670165474çGonçalvesç40000.98
                002ç2345675434544345çJose da SilvaçRural
                002ç2345675433444345çEduardo PereiraçRural
                003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro
                003ç08ç[1-34-10,2-33-1.50,3-40-0.10]çPaulo
                """);
        LineIterator it = new LineIterator(reader);
        SalesBatchProcessing tester = new SalesBatchProcessing();
        var result = tester.processFile("ç", it);
        assertEquals(result.getSalesBySalesman().get("Gonçalves"), BigDecimal.ZERO);
    }

    @Test
    public void givenSalesmanNameContainingTheFileDelimiterThenNoErrorShouldBeThrown() {
        StringReader reader = new StringReader("""
                001ç1234567891234çPedroç50000
                001ç3245678865434çPauloç40000.99
                001ç9745670165474çGonçalvesç40000.98
                002ç2345675434544345çJose da SilvaçRural
                002ç2345675433444345çEduardo PereiraçRural
                003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro
                003ç08ç[1-34-10,2-33-1.50,3-40-0.10]çPaulo
                """);
        LineIterator it = new LineIterator(reader);
        SalesBatchProcessing tester = new SalesBatchProcessing();
        tester.processFile("ç", it);
        assertDoesNotThrow(() -> Exception.class);
    }

    @Test
    public void givenCustomerNameContainingTheFileDelimiterThenNoErrorShouldBeThrown() {
        StringReader reader = new StringReader("""
                001ç1234567891234çPedro Gonçalvesç50000
                001ç3245678865434çPauloç40000.99
                002ç2345675987544345çPedro GonçalvesçRural
                002ç2345675434544345çJose da SilvaçRural
                002ç2345675433444345çEduardo PereiraçRural
                003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro
                003ç08ç[1-34-10,2-33-1.50,3-40-0.10]çPaulo
                """);
        LineIterator it = new LineIterator(reader);
        SalesBatchProcessing tester = new SalesBatchProcessing();
        tester.processFile("ç", it);
        assertDoesNotThrow(() -> Exception.class);
    }

    @Test
    public void givenAnEmptyFileThenNoErrorShouldBeThrown() {
        StringReader reader = new StringReader("");
        LineIterator it = new LineIterator(reader);
        SalesBatchProcessing tester = new SalesBatchProcessing();
        tester.processFile("ç", it);
        assertDoesNotThrow(() -> Exception.class);
    }
}
