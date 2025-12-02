package com.DullPointers.repository;

import com.DullPointers.model.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {
    List<Customer> fetchAll();
    Optional<Customer> findById(UUID id);
    Optional<Customer> findByNameOrPhone(String name);
    void save(Customer customer);
}