package com.DullPointers.model;

import com.DullPointers.repository.SaleRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public abstract class SalesReport extends Report {

    protected abstract List<ISale> getSales();

    @Override
    protected final String createReport() {
        // Generate Product Performance Report as CSV, Including if Product is High or Low Performance
        List<ISale> sales = getSales();
        StringBuilder report = new StringBuilder("DateTime,Cashier,Customer,Total Quantity,Revenue\n");
        for (ISale sale: sales) {
            report.append(sale.getSaleDate().format(DateTimeFormatter.ISO_DATE)).append(",");
            report.append('"').append(sale.getCashier().getUsername()).append('"').append(",");
            report.append('"').append(sale.getCustomer() == null ? sale.getCustomer().getName() : "NULL").append('"').append(",");
            report.append(sale.getItemCount()).append(",");
            report.append(sale.calculateTotalPaid()).append(",");
        }

        return report.toString();
    }
}
