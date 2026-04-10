package com.DullPointers.model;

import com.DullPointers.model.enums.SaleStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


public interface ISale {
//    @JsonSerialize(as = SaleLineItem.class)
//    @JsonDeserialize(as = SaleLineItem.class)
    ISaleLineItem getProductLineItem(IProduct product);

    void addItem(IProduct product, int quantity);

    BigDecimal calculateGrandTotal();

    BigDecimal calculateTotalPaid();

    // Logic: Total - Points Discount
    @JsonIgnore
    BigDecimal getNetPayableAmount();

    boolean isFullyPaid();

    // Getters & Setters
    @JsonSerialize(contentAs = SaleLineItem.class) // Hint for elements during serialization
    @JsonDeserialize(contentAs = SaleLineItem.class)
    List<ISaleLineItem> getItems();

    @JsonSerialize(contentAs = Payment.class)
    @JsonDeserialize(contentAs = Payment.class)
    List<IPayment> getPayments();

    void setStatus(SaleStatus status);

    Long getId();

    void setId(Long id);

    ICustomer getCustomer();

    void setCustomer(ICustomer customer);

    int getPointsRedeemed();

    void setPointsRedeemed(int pointsRedeemed);

    LocalDateTime getSaleDate();

    @JsonSerialize(as = User.class)
    @JsonDeserialize(as = User.class)
    IUser getCashier();

    Integer getItemCount();
}
