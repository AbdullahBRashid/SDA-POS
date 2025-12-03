package com.DullPointers.model;

import java.math.BigDecimal;

public class Product implements IProduct {
    private String barcode;
    private String name;
    private String category;
    private BigDecimal costPrice;
    private BigDecimal sellingPrice;
    private int stockQuantity;
    private int minStockLevel;

    public Product() {}

    public Product(String barcode, String name, BigDecimal sellingPrice, int stockQuantity) {
        this.barcode = barcode;
        this.name = name;
        this.sellingPrice = sellingPrice;
        this.stockQuantity = stockQuantity;
        this.minStockLevel = 5; // Default threshold
    }

    @Override
    public boolean hasSufficientStock(int requestedQuantity) {
        return this.stockQuantity >= requestedQuantity;
    }

    @Override
    public void reduceStock(int quantity) {
        if (!hasSufficientStock(quantity)) {
            throw new IllegalArgumentException("Insufficient stock for product: " + name);
        }
        this.stockQuantity -= quantity;
    }

    @Override
    public boolean isLowStock() {
        return this.stockQuantity <= this.minStockLevel;
    }

    // Getters & Setters
    @Override
    public String getBarcode() { return barcode; }
    @Override
    public String getName() { return name; }
    @Override
    public BigDecimal getSellingPrice() { return sellingPrice; }
    @Override
    public int getStockQuantity() { return stockQuantity; }
    @Override
    public String getIdk() {return "Hello World";}
    @Override
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }
    @Override
    public void setMinStockLevel(int minStockLevel) { this.minStockLevel = minStockLevel; }
}