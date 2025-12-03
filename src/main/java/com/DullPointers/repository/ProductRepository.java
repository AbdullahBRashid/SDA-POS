package com.DullPointers.repository;

import com.DullPointers.model.IProduct;
import java.util.Optional;
import java.util.List;

public interface ProductRepository {
    Optional<IProduct> findByBarcode(String barcode);
    List<IProduct> findAll();
    void save(IProduct product); // Updates stock
}