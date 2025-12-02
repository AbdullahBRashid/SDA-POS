package com.DullPointers.model;

import com.DullPointers.model.enums.SaleStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Sale {
    private Long id; // Assuming you handle ID generation or use UUID string
    private LocalDateTime saleDate;
    private SaleStatus status;
    private User cashier;
    private List<SaleLineItem> items;
    private List<Payment> payments;

    // NEW FIELDS
    private Customer customer;
    private int pointsRedeemed;

    public Sale() {
        this.items = new ArrayList<>();
        this.payments = new ArrayList<>();
    }

    public Sale(User cashier) {
        this();
        this.saleDate = LocalDateTime.now();
        this.status = SaleStatus.PENDING;
        this.cashier = cashier;
        this.pointsRedeemed = 0;
    }

    public void addItem(Product product, int quantity) {
        items.add(new SaleLineItem(product, quantity));
    }

    public BigDecimal calculateGrandTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (SaleLineItem item : items) {
            total = total.add(item.getSubTotal());
        }
        return total;
    }

    public BigDecimal calculateTotalPaid() {
        BigDecimal paid = BigDecimal.ZERO;
        for (Payment p : payments) {
            paid = paid.add(p.getAmount());
        }
        return paid;
    }

    // Logic: Total - Points Discount
    @JsonIgnore
    public BigDecimal getNetPayableAmount() {
        BigDecimal subtotal = calculateGrandTotal();
        BigDecimal discount = BigDecimal.valueOf(pointsRedeemed);
        BigDecimal net = subtotal.subtract(discount);
        return net.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : net;
    }

    public boolean isFullyPaid() {
        return calculateTotalPaid().compareTo(getNetPayableAmount()) >= 0;
    }

    // Getters & Setters
    public List<SaleLineItem> getItems() { return items; }
    public List<Payment> getPayments() { return payments; }
    public void setStatus(SaleStatus status) { this.status = status; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public int getPointsRedeemed() { return pointsRedeemed; }
    public void setPointsRedeemed(int pointsRedeemed) { this.pointsRedeemed = pointsRedeemed; }
    public LocalDateTime getSaleDate() { return saleDate; }
}