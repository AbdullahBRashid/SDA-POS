package com.DullPointers.repository;

import com.DullPointers.model.ICustomer;

import java.util.List;

public interface CustomerRepository {
    List<ICustomer> fetchAll();
    List<ICustomer> search(String key);
    void save(ICustomer customer);
}