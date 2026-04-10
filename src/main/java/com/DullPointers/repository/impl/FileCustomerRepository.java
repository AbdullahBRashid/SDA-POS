package com.DullPointers.repository.impl;

import com.DullPointers.model.Customer;
import com.DullPointers.model.ICustomer;
import com.DullPointers.repository.CustomerRepository;
import com.DullPointers.util.JsonDataStore;

import java.util.ArrayList;
import java.util.List;

public class FileCustomerRepository implements CustomerRepository {
    // Change in .gitignore too if file name or path changes.
    private static final String FILE_PATH = "customers.json";
    private List<ICustomer> database;

    public FileCustomerRepository() {
        this.database = JsonDataStore.load(FILE_PATH, Customer[].class);
    }

    public List<ICustomer> fetchAll() {
        return database;
    }

    public List<ICustomer> search(String key) {
        List<ICustomer> customers = new ArrayList<ICustomer>();
        for (ICustomer customer : database) {
            if (customer.getId().equals(key) || customer.getName().equals(key) || customer.getPhoneNumber().equals(key)) {
                customers.add(customer);
            }
        }
        return customers;
    }

    public void save(ICustomer customer) {
        database.removeIf(c -> c.getId().equals(customer.getId()));
        database.add(customer);
        saveToFile();
    }

    private void saveToFile() {
        JsonDataStore.save(database, FILE_PATH);
    }
}
