package com.DullPointers.model;

import java.math.BigDecimal;

public interface ISaleLineItem {
    // Logic: Calculate line total (Price * Qty) (Req 4)
    BigDecimal getSubTotal();

    // Logic: Update quantity (Req 3)
    void setQuantity(int quantity);

    // Logic: Increment quantity
    void incrementQuantity();

    IProduct getProduct();

    int getQuantity();

    BigDecimal getSnapshotPrice();
}
