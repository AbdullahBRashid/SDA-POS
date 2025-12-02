package com.DullPointers.util;

import com.DullPointers.model.Customer;
import com.DullPointers.model.Sale;
import com.DullPointers.repository.SaleRepository;

import java.math.BigDecimal;
import java.util.List;

public class CustomerAnalytics {
    public static String generateReport(Customer customer, SaleRepository saleRepo) {
        List<Sale> history = saleRepo.findAll().stream()
                .filter(s -> s.getCustomer() != null && s.getCustomer().getId().equals(customer.getId()))
                .toList();

        if (history.isEmpty()) return "No purchase history found.";

        int totalVisits = history.size();
        BigDecimal totalSpent = BigDecimal.ZERO;
        for (Sale s : history) {
            totalSpent = totalSpent.add(s.calculateGrandTotal());
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Total Visits: ").append(totalVisits).append("\n");
        sb.append("Total Spent: $").append(totalSpent).append("\n");
        sb.append("Current Points: ").append(customer.getLoyaltyPoints()).append("\n");
        sb.append("\nRecent Transactions:\n");

        history.stream()
                .sorted((s1, s2) -> s2.getSaleDate().compareTo(s1.getSaleDate()))
                .limit(5)
                .forEach(s -> sb.append("- $").append(s.calculateGrandTotal())
                        .append(" (").append(s.getSaleDate().toLocalDate()).append(")\n"));

        return sb.toString();
    }
}