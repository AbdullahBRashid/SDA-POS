package com.DullPointers.model;

import com.DullPointers.model.enums.SaleStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Sale {
    private Long id; // Database ID
    private LocalDateTime saleDate;
    private SaleStatus status;
    private User cashier; // (Req 15 - Link to user)
    private Customer customer; // (Req 18 - Link to customer)
    private List<SaleLineItem> items;
    private List<Payment> payments; // (Req 7 - Split payments)

    public Sale() {}

    public Sale(User cashier) {
        this.saleDate = LocalDateTime.now();
        this.status = SaleStatus.PENDING;
        this.cashier = cashier;
        this.items = new ArrayList<>();
        this.payments = new ArrayList<>();
    }

    // Logic: Add item to sale
    public void addItem(Product product, int quantity) {
        items.add(new SaleLineItem(product, quantity));
    }

    // Logic: Calculate Grand Total (Req 4)
    public BigDecimal calculateGrandTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (SaleLineItem item : items) {
            total = total.add(item.getSubTotal());
        }
        // Note: You would subtract discounts and add tax here
        return total;
    }

    // Logic: Calculate how much is paid so far
    public BigDecimal calculateTotalPaid() {
        BigDecimal paid = BigDecimal.ZERO;
        for (Payment p : payments) {
            paid = paid.add(p.getAmount());
        }
        return paid;
    }

    // Logic: Check if fully paid
    public boolean isFullyPaid() {
        return calculateTotalPaid().compareTo(calculateGrandTotal()) >= 0;
    }

    // Getters
    public List<SaleLineItem> getItems() { return items; }
    public List<Payment> getPayments() { return payments; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public void setStatus(SaleStatus status) { this.status = status; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
}