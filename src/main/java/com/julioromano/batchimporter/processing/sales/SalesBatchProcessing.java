package com.julioromano.batchimporter.processing.sales;

import com.julioromano.batchimporter.exceptions.ProcessingException;
import com.julioromano.batchimporter.processing.BatchProcessing;
import com.julioromano.batchimporter.processing.BatchResult;
import com.julioromano.batchimporter.processing.sales.pojo.Sale;
import com.julioromano.batchimporter.processing.sales.pojo.SaleItem;
import com.julioromano.batchimporter.processing.sales.pojo.Salesman;
import com.julioromano.batchimporter.utils.AppProperties;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SalesBatchProcessing implements BatchProcessing {

    @Override
    public void process(Path dir, List<Path> files) throws ProcessingException {
        ExecutorService es = Executors.newFixedThreadPool(3);
        List<Callable<SalesBatchResult>> todo = new ArrayList<>(files.size());

        for (Path file : files) {
            todo.add(() -> {
                try (LineIterator it = FileUtils.lineIterator(file.toFile(), "UTF-8")) {
                    String fileDelimiter = AppProperties.getInstance().getProperty(AppProperties.FILE_DELIMITER);
                    return processFile(fileDelimiter, it);
                }
            });
        }

        try {
            List<Future<SalesBatchResult>> results = es.invokeAll(todo);
            SalesBatchResult mainResult = new SalesBatchResult();

            for (Future<SalesBatchResult> result : results) {
                SalesBatchResult dataResult = result.get();

                mainResult.customersQty += dataResult.customersQty;
                mainResult.salesmanQty += dataResult.salesmanQty;

                if (dataResult.mostExpensiveSalePrice.compareTo(mainResult.mostExpensiveSalePrice) > 0) {
                    mainResult.mostExpensiveSale = dataResult.mostExpensiveSale;
                    mainResult.mostExpensiveSalePrice = dataResult.mostExpensiveSalePrice;
                }

                Set<String> keys = dataResult.salesBySalesman.keySet();
                for (String key : keys) {
                    if (!mainResult.salesBySalesman.containsKey(key)) {
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

            mainResult.worstSalesman = worstSalesman;
            produceOutput(mainResult);
        } catch (InterruptedException | ExecutionException e) {
            throw new ProcessingException(e);
        }
    }

    public SalesBatchResult processFile(String fileDelimiter, LineIterator it) {
        SalesBatchResult result = new SalesBatchResult();
        Map<String, Consumer<String>> parsers = getParsersMap(result, fileDelimiter);

        while (it.hasNext()) {
            String line = it.nextLine();
            String identifier = line.substring(0, line.indexOf(fileDelimiter));

            if(parsers.containsKey(identifier)) {
                parsers.get(identifier).accept(line);
            }
        }

        return result;
    }

    private Map<String, Consumer<String>> getParsersMap(SalesBatchResult result, String fileDelimiter) {
        Map<String, Consumer<String>> parsers = new HashMap<>();
        parsers.put(SalesBatchType.SALESMAN.getIdentifier(), line -> {
            handleSalesmanData(result, fileDelimiter, line);
        });
        parsers.put(SalesBatchType.CUSTOMER.getIdentifier(), line -> handleCustomerData(result));
        parsers.put(SalesBatchType.SALE.getIdentifier(), line -> {
            handleSaleData(result, fileDelimiter, line);
        });
        return parsers;
    }

    private void handleSaleData(SalesBatchResult result, String fileDelimiter, String line) {
        String regexSplitter = SalesBatchType.SALE.getRegexSplitter().replaceAll("DEL", fileDelimiter);
        Pattern p = Pattern.compile(regexSplitter);
        Matcher m = p.matcher(line);
        if (m.matches()) {
            String id = m.group(2);
            String items = m.group(3);
            String salesmanName = m.group(4);

            Sale sale = Sale.builder()
                    .id(id).salesmanName(salesmanName).items(items).build();

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

    private int handleCustomerData(SalesBatchResult result) {
        return result.customersQty++;
    }

    private void handleSalesmanData(SalesBatchResult result, String fileDelimiter, String line) {
        String regexSplitter = SalesBatchType.SALESMAN.getRegexSplitter().replaceAll("DEL", fileDelimiter);
        Pattern p = Pattern.compile(regexSplitter);
        Matcher m = p.matcher(line);
        if (m.matches()) {
            Long cpf = Long.parseLong(m.group(2));
            String name = m.group(3);
            BigDecimal salary = new BigDecimal(m.group(4));

            Salesman salesman = Salesman.builder()
                    .cpf(cpf).name(name).salary(salary).build();

            result.salesmanQty++;

            if (!result.salesBySalesman.containsKey(salesman.getName())) {
                result.salesBySalesman.put(salesman.getName(), BigDecimal.ZERO);
            }
        }
    }

    @Override
    public void produceOutput(BatchResult batchResult) throws ProcessingException {
        SalesBatchResult salesBatchResult = (SalesBatchResult) batchResult;
        String outputFileDir = AppProperties.getInstance().getProperty(AppProperties.SALES_DATA_OUT_DIR);
        String fileName = new SimpleDateFormat("yyyyMMddHHmm'.dat'").format(new Date());

        try {
            createDirIfDoesNotExist(outputFileDir);

            FileWriter fileWriter = new FileWriter(outputFileDir + fileName);
            try (PrintWriter printWriter = new PrintWriter(fileWriter)) {
                String outputTpl = "Clientes: %s\nVendedores: %s\nVenda mais cara: %s\nPior vendedor: %s\n";
                printWriter.printf(outputTpl, salesBatchResult.customersQty,
                        salesBatchResult.salesmanQty, salesBatchResult.mostExpensiveSale,
                        salesBatchResult.worstSalesman);
            }
        } catch (IOException e) {
            throw new ProcessingException(e);
        }
    }

    private void createDirIfDoesNotExist(String outputFileDir) {
        File output = new File(outputFileDir);
        if (! output.exists()) {
            if(! output.mkdirs()) {
                throw new ProcessingException("Error creating directories");
            }
        }
    }

    public static class SalesBatchResult extends BatchResult {
        private final HashMap<String, BigDecimal> salesBySalesman = new HashMap<>();
        private int customersQty = 0;
        private int salesmanQty = 0;
        private String mostExpensiveSale = "";
        private BigDecimal mostExpensiveSalePrice = BigDecimal.ZERO;
        private String worstSalesman = "";

        public int getCustomersQty() {
            return customersQty;
        }

        public int getSalesmanQty() {
            return salesmanQty;
        }

        public String getMostExpensiveSale() {
            return mostExpensiveSale;
        }

        public HashMap<String, BigDecimal> getSalesBySalesman() {
            return salesBySalesman;
        }
    }
}
