package com.DullPointers.manager;

public interface IInventoryManager {
    boolean checkStock(String barcode, int quantityRequested);

    void reduceStock(String barcode, int quantity);
}
