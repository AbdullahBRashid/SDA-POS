package com.DullPointers.model;

import com.DullPointers.model.enums.SaleStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ISale {
    ISaleLineItem getProductLineItem(IProduct product);

    void addItem(IProduct product, int quantity);

    BigDecimal calculateGrandTotal();

    BigDecimal calculateTotalPaid();

    // Logic: Total - Points Discount
    @JsonIgnore
    BigDecimal getNetPayableAmount();

    boolean isFullyPaid();

    // Getters & Setters
    List<ISaleLineItem> getItems();

    List<IPayment> getPayments();

    void setStatus(SaleStatus status);

    Long getId();

    void setId(Long id);

    ICustomer getCustomer();

    void setCustomer(ICustomer customer);

    int getPointsRedeemed();

    void setPointsRedeemed(int pointsRedeemed);

    LocalDateTime getSaleDate();

    IUser getCashier();

    Integer getItemCount();
}
