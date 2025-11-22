package com.DullPointers.repository;

import com.DullPointers.model.Product;
import java.util.Optional;
import java.util.List;

public interface ProductRepository {
    Optional<Product> findByBarcode(String barcode);
    List<Product> findAll();
    void save(Product product); // Updates stock
}