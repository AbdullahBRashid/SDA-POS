package com.DullPointers.model;

import com.DullPointers.model.enums.SaleStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Sale implements ISale {
    private Long id; // Assuming you handle ID generation or use UUID string
    private LocalDateTime saleDate;
    private SaleStatus status;
    private IUser cashier;
    private List<ISaleLineItem> items;
    private List<IPayment> payments;

    // NEW FIELDS
    private ICustomer customer;
    private int pointsRedeemed;

    public Sale() {
        this.items = new ArrayList<>();
        this.payments = new ArrayList<>();
    }

    @Override
    public ISaleLineItem getProductLineItem(IProduct product) {
        for  (ISaleLineItem item : items) {
            if (item.getProduct() == product)
                return item;
        }
        return null;
    }

    @Override
    public IUser getCashier() {
        return cashier;
    }

    @Override
    public Integer getItemCount() {
        int count = 0;
        for (ISaleLineItem item : items) {
            count += item.getQuantity();
        }
        return count;
    }

    public Sale(IUser cashier) {
        this();
        this.saleDate = LocalDateTime.now();
        this.status = SaleStatus.PENDING;
        this.cashier = cashier;
        this.pointsRedeemed = 0;
    }

    @Override
    public void addItem(IProduct product, int quantity) {
        items.add(new SaleLineItem(product, quantity));
    }

    @Override
    public BigDecimal calculateGrandTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (ISaleLineItem item : items) {
            total = total.add(item.getSubTotal());
        }
        return total;
    }

    @Override
    public BigDecimal calculateTotalPaid() {
        BigDecimal paid = BigDecimal.ZERO;
        for (IPayment p : payments) {
            paid = paid.add(p.getAmount());
        }
        return paid;
    }

    // Logic: Total - Points Discount
    @JsonIgnore
    @Override
    public BigDecimal getNetPayableAmount() {
        BigDecimal subtotal = calculateGrandTotal();
        BigDecimal discount = BigDecimal.valueOf(pointsRedeemed);
        BigDecimal net = subtotal.subtract(discount);
        return net.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : net;
    }

    @Override
    public boolean isFullyPaid() {
        return calculateTotalPaid().compareTo(getNetPayableAmount()) >= 0;
    }

    // Getters & Setters
    @Override
    public List<ISaleLineItem> getItems() { return items; }
    @Override
    public List<IPayment> getPayments() { return payments; }
    @Override
    public void setStatus(SaleStatus status) { this.status = status; }
    @Override
    public Long getId() { return id; }
    @Override
    public void setId(Long id) { this.id = id; }
    @Override
    public ICustomer getCustomer() { return customer; }
    @Override
    public void setCustomer(ICustomer customer) { this.customer = customer; }
    @Override
    public int getPointsRedeemed() { return pointsRedeemed; }
    @Override
    public void setPointsRedeemed(int pointsRedeemed) { this.pointsRedeemed = pointsRedeemed; }
    @Override
    public LocalDateTime getSaleDate() { return saleDate; }
}