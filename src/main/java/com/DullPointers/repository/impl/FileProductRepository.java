package com.DullPointers.repository.impl;

import com.DullPointers.model.IProduct;
import com.DullPointers.model.Product;
import com.DullPointers.repository.ProductRepository;
import com.DullPointers.util.JsonDataStore;

import java.math.BigDecimal;
import java.util.ArrayList; // <--- Added this import
import java.util.List;
import java.util.Optional;

public class FileProductRepository implements ProductRepository {
    // Change in .gitignore too if file name or path changes.
    private static final String FILE_PATH = "products.json";
    private List<IProduct> database;

    public FileProductRepository() {
        // 1. Load data from file on startup
        this.database = JsonDataStore.load(FILE_PATH, Product[].class);

        // 2. If file is empty, seed some dummy data (Optional)
        if (database.isEmpty()) {
            database.add(new Product("111", "Coke", new BigDecimal("1.50"), 100));
            database.add(new Product("222", "Chips", new BigDecimal("2.00"), 50));
            saveToFile(); // Write the seed data to file
        }
    }

    private void saveToFile() {
        JsonDataStore.save(database, FILE_PATH);
    }

    @Override
    public Optional<IProduct> findByBarcode(String barcode) {
        return database.stream()
                .filter(p -> p.getBarcode().equals(barcode))
                .findFirst();
    }

    @Override
    public void save(IProduct product) {
        // Remove old version if exists (to update)
        database.removeIf(p -> p.getBarcode().equals(product.getBarcode()));
        database.add(product);

        // Commit to file immediately
        saveToFile();
    }

    // --- NEW METHOD ---
    @Override
    public List<IProduct> findAll() {
        // Return a copy to protect the internal list
        return new ArrayList<>(database);
    }
}