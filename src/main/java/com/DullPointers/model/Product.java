package com.DullPointers.model;

import java.math.BigDecimal;

public class Product {
    private String barcode;
    private String name;
    private String category;
    private BigDecimal costPrice;
    private BigDecimal sellingPrice;
    private int stockQuantity;
    private int minStockLevel;

    public Product(String barcode, String name, BigDecimal sellingPrice, int stockQuantity) {
        this.barcode = barcode;
        this.name = name;
        this.sellingPrice = sellingPrice;
        this.stockQuantity = stockQuantity;
        this.minStockLevel = 5; // Default threshold
    }

    public boolean hasSufficientStock(int requestedQuantity) {
        return this.stockQuantity >= requestedQuantity;
    }

    public void reduceStock(int quantity) {
        if (!hasSufficientStock(quantity)) {
            throw new IllegalArgumentException("Insufficient stock for product: " + name);
        }
        this.stockQuantity -= quantity;
    }

    public boolean isLowStock() {
        return this.stockQuantity <= this.minStockLevel;
    }

    // Getters & Setters
    public String getBarcode() { return barcode; }
    public String getName() { return name; }
    public BigDecimal getSellingPrice() { return sellingPrice; }
    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }
    public void setMinStockLevel(int minStockLevel) { this.minStockLevel = minStockLevel; }
}