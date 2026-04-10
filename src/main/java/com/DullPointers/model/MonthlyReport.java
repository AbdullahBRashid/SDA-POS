package com.DullPointers.model;

import com.DullPointers.repository.SaleRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class MonthlyReport extends SalesReport {
    private LocalDate date;
    private SaleRepository sr;

    public MonthlyReport(LocalDate date, SaleRepository sr) {
        super();
        this.sr = sr;
        this.date = date;
    }

    @Override
    protected List<ISale> getSales() {
        LocalDateTime monthStart = date.atStartOfDay().minusDays(date.getDayOfMonth());
        return sr.findInDuration(monthStart, monthStart.plusMonths(1));
    }
}
