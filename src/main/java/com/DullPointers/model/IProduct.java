package com.DullPointers.model;

import java.math.BigDecimal;

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
