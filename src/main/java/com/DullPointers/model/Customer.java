package com.DullPointers.model;

import java.util.UUID;

public class Customer {
    private String id;
    private String phoneNumber;
    private String name;
    private int loyaltyPoints;

    public Customer() {}

    public Customer(String phoneNumber, String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.loyaltyPoints = 0;
    }

    public void addLoyaltyPoints(int points) { this.loyaltyPoints += points; }

    public void consumeLoyaltyPoints(int points) {
        if (points > this.loyaltyPoints) throw new IllegalArgumentException("Insufficient points");
        this.loyaltyPoints -= points;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getPhoneNumber() { return phoneNumber; }
    public int getLoyaltyPoints() { return loyaltyPoints; }
}