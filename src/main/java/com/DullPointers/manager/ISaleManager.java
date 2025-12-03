package com.DullPointers.manager;

import com.DullPointers.model.ICustomer;
import com.DullPointers.model.ISaleLineItem;
import com.DullPointers.model.Sale;
import com.DullPointers.model.enums.PaymentMethod;

import java.math.BigDecimal;

public interface ISaleManager {
    // Req 1: Start a new transaction
    void startNewSale();

    // Req 2 & 10: Add item by scanning barcode
    void addItemToSale(String barcode, int quantity);

    void removeItemFromSale(ISaleLineItem item);

    // Link Customer & Reset previous points selection
    void setCustomer(ICustomer customer);

    // Toggle Redemption: Use Max points up to bill amount
    void toggleLoyaltyRedemption(boolean enable);

    // Req 7: Add Payment (Split payment logic)
    void addPayment(BigDecimal amount, PaymentMethod method);

    // Req 6: Finalize Sale & Generate Receipt
    void completeSale();

    Sale getCurrentSale();
}
