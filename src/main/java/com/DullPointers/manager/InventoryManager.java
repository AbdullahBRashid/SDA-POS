package com.DullPointers.manager;

import com.DullPointers.model.Product;
import com.DullPointers.repository.ProductRepository;
import java.util.Optional;

public class InventoryManager {
    private final ProductRepository productRepository;
    private final NotificationManager notificationManager;

    public InventoryManager(ProductRepository productRepository, NotificationManager notificationManager) {
        this.productRepository = productRepository;
        this.notificationManager = notificationManager;
    }

    public boolean checkStock(String barcode, int quantityRequested) {
        Optional<Product> productOpt = productRepository.findByBarcode(barcode);
        return productOpt.isPresent() && productOpt.get().getStockQuantity() >= quantityRequested;
    }

    public void reduceStock(String barcode, int quantity) {
        Product product = productRepository.findByBarcode(barcode)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        product.reduceStock(quantity);
        productRepository.save(product);

        // Warning Logic
        if (product.getStockQuantity() <= 5) {
            String msg = "Low Stock: " + product.getName() + " (Qty: " + product.getStockQuantity() + ")";
            notificationManager.addAlert(msg);
        }
    }
}