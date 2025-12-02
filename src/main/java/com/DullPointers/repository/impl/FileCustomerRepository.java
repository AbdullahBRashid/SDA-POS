package com.DullPointers.repository.impl;

import com.DullPointers.model.Customer;
import com.DullPointers.repository.CustomerRepository;
import com.DullPointers.util.JsonDataStore;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

public class FileCustomerRepository implements CustomerRepository {
    // Change in .gitignore too if file name or path changes.
    private static final String FILE_PATH = "customers.json";
    private List<Customer> database;

    public FileCustomerRepository() {
        this.database = JsonDataStore.load(FILE_PATH, Customer[].class);
    }

    public List<Customer> fetchAll() {
        return database;
    }

    public Optional<Customer> findById(UUID id) {
        for (Customer customer : database) {
            if (customer.getId().equals(id)) {
                return Optional.of(customer);
            }
        }
        return Optional.empty();
    }

    public Optional<Customer> findByNameOrPhone(String key) {
        for (Customer customer : database) {
            if (customer.getName().equals(key) || customer.getPhoneNumber().equals(key)) {
                return Optional.of(customer);
            }
        }
        return Optional.empty();
    }

    public void save(Customer customer) {
        database.removeIf(c -> c.getId().equals(customer.getId()));
        database.add(customer);
        saveToFile();
    }

    private void saveToFile() {
        JsonDataStore.save(database, FILE_PATH);
    }
}
