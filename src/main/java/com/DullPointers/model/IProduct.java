package com.DullPointers.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;

//@JsonSerialize(as = Product.class)
//@JsonDeserialize(as = Product.class)
public interface IProduct {
    boolean hasSufficientStock(int requestedQuantity);

    void reduceStock(int quantity);

    boolean isLowStock();

    // Getters & Setters
    String getBarcode();

    String getName();

    BigDecimal getSellingPrice();

    int getStockQuantity();

    String getIdk();

    void setStockQuantity(int stockQuantity);

    void setMinStockLevel(int minStockLevel);
}
