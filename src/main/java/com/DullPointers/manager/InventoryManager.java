package com.DullPointers.manager;

import com.DullPointers.model.IProduct;
import com.DullPointers.repository.ProductRepository;
import java.util.Optional;

public class InventoryManager implements IInventoryManager {
    private final ProductRepository productRepository;
    private final INotificationManager notificationManager;

    public InventoryManager(ProductRepository productRepository, INotificationManager notificationManager) {
        this.productRepository = productRepository;
        this.notificationManager = notificationManager;
    }

    @Override
    public boolean checkStock(String barcode, int quantityRequested) {
        Optional<IProduct> productOpt = productRepository.findByBarcode(barcode);
        return productOpt.isPresent() && productOpt.get().getStockQuantity() >= quantityRequested;
    }

    @Override
    public void reduceStock(String barcode, int quantity) {
        IProduct product = productRepository.findByBarcode(barcode)
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