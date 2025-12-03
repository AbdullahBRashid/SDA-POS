package com.DullPointers.model;

import com.DullPointers.repository.SaleRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class WeeklyReport extends SalesReport {
    private LocalDate date;
    private SaleRepository sr;

    public WeeklyReport(LocalDate date, SaleRepository sr) {
        super();
        this.sr = sr;
        this.date = date;
    }

    @Override
    protected List<ISale> getSales() {
        LocalDateTime weekStart = date.atStartOfDay().minusDays(date.getDayOfWeek().getValue());
        return sr.findInDuration(weekStart, weekStart.plusWeeks(1));
    }
}
