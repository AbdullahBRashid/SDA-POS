package com.DullPointers.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;

public class SaleLineItem implements ISaleLineItem {
    private IProduct product;
    private int quantity;
    private BigDecimal snapshotPrice; // The price AT THE MOMENT of sale

    public SaleLineItem() {}

    public SaleLineItem(IProduct product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        // Capture the price now. If product price changes later, this record stays correct.
        this.snapshotPrice = product.getSellingPrice();
    }

    // Logic: Calculate line total (Price * Qty) (Req 4)
    @Override
    public BigDecimal getSubTotal() {
        return snapshotPrice.multiply(BigDecimal.valueOf(quantity));
    }

    // Logic: Update quantity (Req 3)
    @Override
    public void setQuantity(int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
        this.quantity = quantity;
    }

    // Logic: Increment quantity
    @Override
    public void incrementQuantity() {
        this.quantity++;
    }

    @Override
    @JsonSerialize(as = Product.class)
    @JsonDeserialize(as = Product.class)
    public IProduct getProduct() { return product; }

    @Override
    public int getQuantity() { return quantity; }
    @Override
    public BigDecimal getSnapshotPrice() { return snapshotPrice; }
}