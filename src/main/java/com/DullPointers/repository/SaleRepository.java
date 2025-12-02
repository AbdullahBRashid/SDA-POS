package com.DullPointers.repository;

import com.DullPointers.model.Sale;

import java.util.List;

public interface SaleRepository {
    void save(Sale sale);
    List<Sale> findAll();
}