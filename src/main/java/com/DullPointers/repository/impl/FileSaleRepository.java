package com.DullPointers.repository.impl;

import com.DullPointers.model.IProduct;
import com.DullPointers.model.ISale;
import com.DullPointers.model.Sale;
import com.DullPointers.repository.SaleRepository;
import com.DullPointers.util.JsonDataStore;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileSaleRepository implements SaleRepository {
    // Change in .gitignore too if file name or path changes.
    private static final String FILE_PATH = "sales.json";
    private final List<ISale> database;

    public FileSaleRepository() {
        this.database = JsonDataStore.load(FILE_PATH, Sale[].class);
    }

    @Override
    public List<ISale> findAll() {
        return database;
    }

    @Override
    public void save(ISale sale) {
        // Generate ID if missing
        if (sale.getId() == null) {
            long maxId = database.stream().mapToLong(ISale::getId).max().orElse(0);
            sale.setId(maxId + 1);
        }

        // Update list
        database.removeIf(s -> s.getId().equals(sale.getId()));
        database.add(sale);

        // Save to disk
        JsonDataStore.save(database, FILE_PATH);
        System.out.println("Sale saved to " + FILE_PATH);
    }

    @Override
    public List<ISale> getSalesByProduct(IProduct product) {
        List<ISale> sales = new ArrayList<>();
        for (ISale sale : database) {
            if (sale.getProductLineItem(product) != null)
                sales.add(sale);
        }
        return sales;
    }

    @Override
    // Start Inclusive, End Exclusive
    public List<ISale> findInDuration(LocalDateTime start, LocalDateTime end) {
        List<ISale> sales = new ArrayList<>();
        for (ISale sale : database) {
            if (sale.getSaleDate().isBefore(start)) continue;
            if (sale.getSaleDate().isBefore(end)) sales.add(sale);
        }
        return sales;
    }
}