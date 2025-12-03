package com.DullPointers.repository.impl;

import com.DullPointers.model.Sale;
import com.DullPointers.repository.SaleRepository;
import com.DullPointers.util.JsonDataStore;
import java.util.List;

public class FileSaleRepository implements SaleRepository {
    // Change in .gitignore too if file name or path changes.
    private static final String FILE_PATH = "sales.json";
    private final List<Sale> database;

    public FileSaleRepository() {
        this.database = JsonDataStore.load(FILE_PATH, Sale[].class);
    }

    @Override
    public List<Sale> findAll() {
        return database;
    }

    @Override
    public void save(Sale sale) {
        // Generate ID if missing
        if (sale.getId() == null) {
            long maxId = database.stream().mapToLong(Sale::getId).max().orElse(0);
            sale.setId(maxId + 1);
        }

        // Update list
        database.removeIf(s -> s.getId().equals(sale.getId()));
        database.add(sale);

        // Save to disk
        JsonDataStore.save(database, FILE_PATH);
        System.out.println("Sale saved to " + FILE_PATH);
    }
}