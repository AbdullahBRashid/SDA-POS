package com.DullPointers.model;

import java.math.BigDecimal;

public class SaleLineItem {
    private Product product;
    private int quantity;
    private BigDecimal snapshotPrice; // The price AT THE MOMENT of sale

    public SaleLineItem() {}

    public SaleLineItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        // Capture the price now. If product price changes later, this record stays correct.
        this.snapshotPrice = product.getSellingPrice();
    }

    // Logic: Calculate line total (Price * Qty) (Req 4)
    public BigDecimal getSubTotal() {
        return snapshotPrice.multiply(BigDecimal.valueOf(quantity));
    }

    // Logic: Update quantity (Req 3)
    public void setQuantity(int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
        this.quantity = quantity;
    }

    // Logic: Increment quantity
    public void incrementQuantity() {
        this.quantity++;
    }

    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public BigDecimal getSnapshotPrice() { return snapshotPrice; }
}