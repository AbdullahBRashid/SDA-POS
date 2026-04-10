package com.DullPointers.model;

import com.DullPointers.repository.SaleRepository;

import java.time.LocalDate;
import java.util.List;

public class DailyReport extends SalesReport {
    private LocalDate date;
    private SaleRepository sr;

    public DailyReport(LocalDate date, SaleRepository sr) {
        super();
        this.sr = sr;
        this.date = date;
    }

    @Override
    protected List<ISale> getSales() {
        return sr.findInDuration(date.atStartOfDay(), date.atStartOfDay().plusDays(1));
    }
}
