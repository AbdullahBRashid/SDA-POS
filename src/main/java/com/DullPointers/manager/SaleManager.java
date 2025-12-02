package com.DullPointers.manager;

import com.DullPointers.model.*;
import com.DullPointers.model.enums.PaymentMethod;
import com.DullPointers.model.enums.SaleStatus;
import com.DullPointers.repository.CustomerRepository;
import com.DullPointers.repository.ProductRepository;
import com.DullPointers.repository.SaleRepository;

import java.math.BigDecimal;

public class SaleManager {
    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final InventoryManager inventoryManager;
    private final AuthManager authManager;
    private final CustomerRepository customerRepository;

    private Sale currentSale;

    public SaleManager(SaleRepository saleRepository,
                       ProductRepository productRepository,
                       InventoryManager inventoryManager,
                       AuthManager authManager,
                       CustomerRepository customerRepository) {
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
        this.inventoryManager = inventoryManager;
        this.authManager = authManager;
        this.customerRepository = customerRepository;
    }

    public void startNewSale() {
        this.currentSale = new Sale(authManager.getCurrentUser());
    }

    public void addItemToSale(String barcode, int quantity) {
        if (currentSale == null) startNewSale();
        if (!inventoryManager.checkStock(barcode, quantity)) {
            throw new IllegalStateException("Item Out of Stock: " + barcode);
        }
        Product product = productRepository.findByBarcode(barcode)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        for (SaleLineItem item : currentSale.getItems()) {
            if (item.getProduct().getBarcode().equals(barcode)) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        currentSale.addItem(product, quantity);
    }

    public void removeItemFromSale(SaleLineItem item) {
        if (currentSale != null) currentSale.getItems().remove(item);
    }

    // Link Customer & Reset previous points selection
    public void setCustomer(Customer customer) {
        if (currentSale == null) startNewSale();
        currentSale.setCustomer(customer);
        currentSale.setPointsRedeemed(0);
    }

    // Toggle Redemption: Use Max points up to bill amount
    public void toggleLoyaltyRedemption(boolean enable) {
        if (currentSale == null || currentSale.getCustomer() == null) return;

        if (enable) {
            Customer c = currentSale.getCustomer();
            BigDecimal billTotal = currentSale.calculateGrandTotal();
            int maxPointsUsable = Math.min(c.getLoyaltyPoints(), billTotal.intValue());
            currentSale.setPointsRedeemed(maxPointsUsable);
        } else {
            currentSale.setPointsRedeemed(0);
        }
    }

    public void addPayment(BigDecimal amount, PaymentMethod method) {
        if (currentSale == null) throw new IllegalStateException("No active sale");
        Payment payment = new Payment(amount, method, "REF-" + System.currentTimeMillis());
        currentSale.getPayments().add(payment);
    }

    public void completeSale() {
        if (currentSale == null) throw new IllegalStateException("No active sale");

        // 1. Validate Payment based on NET PAYABLE
        BigDecimal netPayable = currentSale.getNetPayableAmount();
        if (currentSale.calculateTotalPaid().compareTo(netPayable) < 0) {
            throw new IllegalStateException("Insufficient Payment.");
        }

        // 2. Consume Loyalty Points (Deduct from Customer)
        if (currentSale.getPointsRedeemed() > 0 && currentSale.getCustomer() != null) {
            Customer c = currentSale.getCustomer();
            c.consumeLoyaltyPoints(currentSale.getPointsRedeemed());
            customerRepository.save(c);
        }

        // 3. Update Inventory (Triggers alerts)
        currentSale.getItems().forEach(item -> {
            inventoryManager.reduceStock(item.getProduct().getBarcode(), item.getQuantity());
        });

        // 4. Earn New Points (Grand Total / 100, Rounded UP)
        if (currentSale.getCustomer() != null) {
            BigDecimal total = currentSale.calculateGrandTotal();
            int pointsEarned = (int) Math.ceil(total.doubleValue() / 100.0);

            if (pointsEarned > 0) {
                currentSale.getCustomer().addLoyaltyPoints(pointsEarned);
                customerRepository.save(currentSale.getCustomer());
            }
        }

        // 5. Finalize
        currentSale.setStatus(SaleStatus.COMPLETED);
        saleRepository.save(currentSale);
        System.out.println("Receipt Generated. ID: " + currentSale.getId());
        this.currentSale = null;
    }

    public Sale getCurrentSale() { return currentSale; }
}