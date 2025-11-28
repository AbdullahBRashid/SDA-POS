package com.DullPointers.manager;

import com.DullPointers.model.Payment;
import com.DullPointers.model.Product;
import com.DullPointers.model.Sale;
import com.DullPointers.model.SaleLineItem;
import com.DullPointers.model.enums.PaymentMethod;
import com.DullPointers.model.enums.SaleStatus;
import com.DullPointers.repository.ProductRepository;
import com.DullPointers.repository.SaleRepository;

import java.math.BigDecimal;

public class SaleManager {
    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final InventoryManager inventoryManager;
    private final AuthManager authManager;

    private Sale currentSale; // Represents the active "Cart"

    public SaleManager(SaleRepository saleRepository,
                       ProductRepository productRepository,
                       InventoryManager inventoryManager,
                       AuthManager authManager) {
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
        this.inventoryManager = inventoryManager;
        this.authManager = authManager;
    }

    // Req 1: Start a new transaction
    public void startNewSale() {
        this.currentSale = new Sale(authManager.getCurrentUser());
    }

    // Req 2 & 10: Add item by scanning barcode
    public void addItemToSale(String barcode, int quantity) {
        if (currentSale == null) startNewSale();

        // 1. Check Inventory First (Delegation)
        if (!inventoryManager.checkStock(barcode, quantity)) {
            throw new IllegalStateException("Item Out of Stock: " + barcode);
        }

        // 2. Find product
        Product product = productRepository.findByBarcode(barcode)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        // 3. Add to Sale
        for (SaleLineItem item : currentSale.getItems()) {
            if (item.getProduct().getBarcode().equals(barcode)) {
                item.incrementQuantity();
                return;
            }
        }

        currentSale.addItem(product, quantity);
    }

    public void removeItemFromSale(SaleLineItem item) {
        currentSale.getItems().removeIf(it -> it.equals(item));
    }

    // Req 7: Add Payment (Split payment logic)
    public void addPayment(BigDecimal amount, PaymentMethod method) {
        if (currentSale == null) throw new IllegalStateException("No active sale");

        Payment payment = new Payment(amount, method, "REF-" + System.currentTimeMillis());
        currentSale.getPayments().add(payment);
    }

    // Req 6: Finalize Sale & Generate Receipt
    public void completeSale() {
        if (currentSale == null) throw new IllegalStateException("No active sale");

        // 1. Validate Payment
        if (!currentSale.isFullyPaid()) {
            BigDecimal remaining = currentSale.calculateGrandTotal().subtract(currentSale.calculateTotalPaid());
            throw new IllegalStateException("Insufficient Payment. Remaining: " + remaining);
        }

        // 2. Update Inventory (Req 9)
        currentSale.getItems().forEach(item -> {
            inventoryManager.reduceStock(item.getProduct().getBarcode(), item.getQuantity());
        });

        // 3. Finalize Status
        currentSale.setStatus(SaleStatus.COMPLETED);

        // 4. Save to DB
        saleRepository.save(currentSale);

        // 5. Generate Receipt (Req 6) - In real app, this calls a Printer
        System.out.println("Receipt Generated for Sale ID: " + currentSale.getId());

        // Clear cart
        this.currentSale = null;
    }

    public Sale getCurrentSale() {
        return currentSale;
    }
}