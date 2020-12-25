package com.julioromano.batchimporter.processing.sales;

import com.julioromano.batchimporter.processing.BatchProcessing;
import com.julioromano.batchimporter.processing.sales.converter.SalesDataConverter;
import com.julioromano.batchimporter.processing.sales.converter.impl.CustomerConverter;
import com.julioromano.batchimporter.processing.sales.converter.impl.SaleConverter;
import com.julioromano.batchimporter.processing.sales.converter.impl.SalesmanConverter;
import com.julioromano.batchimporter.processing.sales.pojo.Customer;
import com.julioromano.batchimporter.processing.sales.pojo.Sale;
import com.julioromano.batchimporter.processing.sales.pojo.SaleItem;
import com.julioromano.batchimporter.processing.sales.pojo.Salesman;
import com.julioromano.batchimporter.utils.AppProperties;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

public class SalesBatchProcessing implements BatchProcessing {

    private static final String SALESMAN = "001";
    private static final String CUSTOMER = "002";
    private static final String SALE = "003";

    public static class DataResult {
        private int customersQty = 0;
        private int salesmanQty = 0;
        private String mostExpensiveSale = "";
        private BigDecimal mostExpensiveSalePrice = BigDecimal.ZERO;
        private HashMap<String, BigDecimal> salesBySalesman = new HashMap<>();

        public int getCustomersQty() {
            return customersQty;
        }

        public int getSalesmanQty() {
            return salesmanQty;
        }

        public String getMostExpensiveSale() {
            return mostExpensiveSale;
        }

        public BigDecimal getMostExpensiveSalePrice() {
            return mostExpensiveSalePrice;
        }

        public HashMap<String, BigDecimal> getSalesBySalesman() {
            return salesBySalesman;
        }
    }

    @Override
    public void process(Path dir, List<Path> files) throws InterruptedException, ExecutionException, IOException {
        ExecutorService es = Executors.newFixedThreadPool(3);
        List<Callable<DataResult>> todo = new ArrayList<Callable<DataResult>>(files.size());

        for (Path file : files) {
            todo.add(() -> {
                try (LineIterator it = FileUtils.lineIterator(file.toFile(), "UTF-8")) {
                    String fileDelimiter = AppProperties.getInstance().getProperty(AppProperties.FILE_DELIMITER);
                    return processFile(fileDelimiter, it);
                }
            });
        }

        List<Future<DataResult>> results = es.invokeAll(todo);

        DataResult mainResult = new DataResult();
        Map<String, BigDecimal> salesBySalesman = new HashMap<>();

        for (Future<DataResult> result : results) {
            DataResult dataResult = result.get();

            mainResult.customersQty += dataResult.customersQty;
            mainResult.salesmanQty += dataResult.salesmanQty;

            if (dataResult.mostExpensiveSalePrice.compareTo(mainResult.mostExpensiveSalePrice) > 0) {
                mainResult.mostExpensiveSale = dataResult.mostExpensiveSale;
                mainResult.mostExpensiveSalePrice = dataResult.mostExpensiveSalePrice;
            }

            Set<String> keys = dataResult.salesBySalesman.keySet();
            for (String key : keys) {
                if (! mainResult.salesBySalesman.containsKey(key)) {
                    mainResult.salesBySalesman.put(key, BigDecimal.ZERO);
                }

                BigDecimal sales = mainResult.salesBySalesman.get(key);
                sales = sales.add(dataResult.salesBySalesman.get(key));
                mainResult.salesBySalesman.put(key, sales);
            }
        }

        Set<String> salesmanSet = mainResult.salesBySalesman.keySet();
        String worstSalesman = "";
        BigDecimal worstSalesmanTotalPrice = BigDecimal.ZERO;
        for (String salesmanKey : salesmanSet) {
            BigDecimal salesmanTotalPrice = mainResult.salesBySalesman.get(salesmanKey);
            if (worstSalesmanTotalPrice.compareTo(BigDecimal.ZERO) == 0
                    || salesmanTotalPrice.compareTo(worstSalesmanTotalPrice) < 0) {
                worstSalesman = salesmanKey;
                worstSalesmanTotalPrice = salesmanTotalPrice;
            }
        }

        produceOutput(mainResult.customersQty, mainResult.salesmanQty, mainResult.mostExpensiveSale, worstSalesman);
    }

    public DataResult processFile(String fileDelimiter, LineIterator it) {
        DataResult result = new DataResult();

        while (it.hasNext()) {
            String line = it.nextLine();
            String[] parts = line.split(fileDelimiter);

            if (SALESMAN.equals(parts[0])) {
                SalesDataConverter<Salesman> salesmanConverter = new SalesmanConverter();
                Salesman salesman = salesmanConverter.convert(parts);

                result.salesmanQty++;

                if (!result.salesBySalesman.containsKey(salesman.getName())) {
                    result.salesBySalesman.put(salesman.getName(), BigDecimal.ZERO);
                }
            } else if (CUSTOMER.equals(parts[0])) {
                SalesDataConverter<Customer> customerConverter = new CustomerConverter();
                Customer customer = customerConverter.convert(parts);

                result.customersQty++;
            } else if (SALE.equals(parts[0])) {
                SalesDataConverter<Sale> saleConverter = new SaleConverter();
                Sale sale = saleConverter.convert(parts);

                BigDecimal salePrice = BigDecimal.ZERO;
                for (SaleItem saleItem : sale.getItems()) {
                    salePrice = salePrice.add(saleItem.getPrice());
                }

                if (salePrice.compareTo(result.mostExpensiveSalePrice) > 0) {
                    result.mostExpensiveSale = sale.getId();
                    result.mostExpensiveSalePrice = salePrice;
                }

                if (!result.salesBySalesman.containsKey(sale.getSalesmanName())) {
                    result.salesBySalesman.put(sale.getSalesmanName(), BigDecimal.ZERO);
                }

                BigDecimal totalSalesmanPrice = result.salesBySalesman.get(sale.getSalesmanName());
                totalSalesmanPrice = totalSalesmanPrice.add(salePrice);
                result.salesBySalesman.put(sale.getSalesmanName(), totalSalesmanPrice);
            }
        }

        return result;
    }

    @Override
    public void produceOutput(int customersQty, int salesmanQty, String mostExpensiveSale, String worstSalesman) throws IOException {
        String outputFileDir = AppProperties.getInstance().getProperty(AppProperties.DATA_OUT_DIR);
        String fileName = new SimpleDateFormat("yyyyMMddHHmm'.dat'").format(new Date());

        FileWriter fileWriter = new FileWriter(outputFileDir + fileName);
        try (PrintWriter printWriter = new PrintWriter(fileWriter)) {
            String outputTpl = "Clientes: %s\nVendedores: %s\nVenda mais cara: %s\nPior vendedor: %s\n";
            System.out.println(String.format(outputTpl, customersQty, salesmanQty, mostExpensiveSale, worstSalesman));
            printWriter.printf(outputTpl, customersQty, salesmanQty, mostExpensiveSale, worstSalesman);
        }
    }
}
