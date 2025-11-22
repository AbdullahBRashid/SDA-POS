package com.DullPointers.repository;

import com.DullPointers.model.Product;
import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findByBarcode(String barcode);
    void save(Product product); // Updates stock
}