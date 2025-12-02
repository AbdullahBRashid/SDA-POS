package com.DullPointers.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Customer {
    private UUID id;
    private String phoneNumber; // Primary Search Key (Req 21)
    private String name;
    private int loyaltyPoints; // Req 19
    private List<Sale> purchaseHistory;

    public Customer() {}

    public Customer(String phoneNumber, String name) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.loyaltyPoints = 0;
        this.purchaseHistory = new ArrayList<>();
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getPhoneNumber() { return phoneNumber; }

    public int getLoyaltyPoints() { return loyaltyPoints; }
    public void addLoyaltyPoints(int points) { this.loyaltyPoints += points; }
    public void consumeLoyaltyPoints(int points) { this.loyaltyPoints -= points; }

    public void addPurchase(Sale sale) { this.purchaseHistory.add(sale); }
    public List<Sale> getPurchaseHistory() { return purchaseHistory; }
}