package com.DullPointers.model;

import com.DullPointers.repository.SaleRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ProductPerformanceReport extends Report {

    private static class MonthlyPerformance {
        public Integer sale = 0;
        public BigDecimal revenue = BigDecimal.ZERO;
    }

    SaleRepository sr;
    IProduct product;
    public ProductPerformanceReport(SaleRepository sr, IProduct product) {
        this.sr = sr;
        this.product = product;
    }

    @Override
    protected String createReport() {
        // Generate Product Performance Report as CSV, Including if Product is High or Low Performance
        List<ISale> sales = sr.getSalesByProduct(product);
        StringBuilder report = new StringBuilder("Month,Sale,Revenue\n");
        HashMap<String, MonthlyPerformance> map = new HashMap<>();
        for (ISale sale : sales) {
            LocalDateTime date = sale.getSaleDate();
            ISaleLineItem lineItem = sale.getProductLineItem(product);
            String month = date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            month += " ";
            month += date.getYear();

            if (!map.containsKey(month)) {
                map.put(month, new MonthlyPerformance());
            }
            MonthlyPerformance pm = map.get(month);
            pm.revenue = pm.revenue.add(lineItem.getSubTotal());
            pm.sale += lineItem.getQuantity();
        }

        for (String month : map.keySet()) {
            MonthlyPerformance pm = map.get(month);
            report.append(month).append(",");
            report.append(pm.sale).append(",");
            report.append(pm.revenue).append("\n");
        }

        return report.toString();
    }
}
