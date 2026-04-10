package com.DullPointers.model;

import com.DullPointers.repository.SaleRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CashierWiseReport extends Report {

    private static class MonthlyPerformance {
        public Integer sale = 0;
        public BigDecimal revenue = BigDecimal.ZERO;
    }

    SaleRepository sr;
    public CashierWiseReport(SaleRepository sr) {
        this.sr = sr;
    }

    @Override
    protected String createReport() {
        // Generate Product Performance Report as CSV, Including if Product is High or Low Performance
        List<ISale> sales = sr.findAll();
        StringBuilder report = new StringBuilder("Cashier,Month,Sale,Revenue\n");
        HashMap<String, HashMap<String, MonthlyPerformance>> map = new HashMap<>();
        for (ISale sale : sales) {
            IUser cashier = sale.getCashier();
            if (!map.containsKey(cashier.getUsername())) map.put(cashier.getUsername(), new HashMap<>());
            HashMap<String, MonthlyPerformance> CashierMap = map.get(cashier.getUsername());
            LocalDateTime date = sale.getSaleDate();
            String month = date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            month += " ";
            month += date.getYear();

            if (!map.containsKey(month)) {
                CashierMap.put(month, new MonthlyPerformance());
            }
            MonthlyPerformance pm = CashierMap.get(month);
            pm.revenue = pm.revenue.add(sale.calculateTotalPaid());
            pm.sale += sale.getItemCount();
        }

        for (String cashier : map.keySet()) {
            HashMap<String, MonthlyPerformance> CashierMap = map.get(cashier);
            for (String month: CashierMap.keySet()) {
                MonthlyPerformance pm = CashierMap.get(month);
                report.append(cashier).append(",");
                report.append(month).append(",");
                report.append(pm.sale).append(",");
                report.append(pm.revenue).append("\n");
            }
        }

        return report.toString();
    }
}
