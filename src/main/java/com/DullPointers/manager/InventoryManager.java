package com.DullPointers.manager;

import com.DullPointers.model.Product;
import com.DullPointers.repository.ProductRepository;
import java.util.Optional;

public class InventoryManager {
    private final ProductRepository productRepository;

    // Constructor Injection (DIP compliant)
    public InventoryManager(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Logic for Req 10: Restrict sale of out-of-stock items
    public boolean checkStock(String barcode, int quantityRequested) {
        Optional<Product> productOpt = productRepository.findByBarcode(barcode);

        if (productOpt.isEmpty()) return false;

        return productOpt.get().hasSufficientStock(quantityRequested);
    }

    // Logic for Req 9: Update stock levels
    public void reduceStock(String barcode, int quantity) {
        Product product = productRepository.findByBarcode(barcode)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        product.reduceStock(quantity);
        productRepository.save(product); // Save new quantity to DB

        // Logic for Req 11: Notify when low
        if (product.isLowStock()) {
            System.out.println("WARNING: Low Stock for " + product.getName());
            // In a real app, this would trigger a UI Alert or Email
        }
    }
}