package com.DullPointers.repository;

import com.DullPointers.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {
    List<Customer> fetchAll();
    List<Customer> search(String key);
    void save(Customer customer);
}