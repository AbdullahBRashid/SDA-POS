package com.DullPointers.repository;

import com.DullPointers.model.IProduct;
import com.DullPointers.model.ISale;

import java.time.LocalDateTime;
import java.util.List;

public interface SaleRepository {
    void save(ISale sale);
    List<ISale> getSalesByProduct(IProduct product);
    List<ISale> findAll();
    List<ISale> findInDuration(LocalDateTime start, LocalDateTime end);
}