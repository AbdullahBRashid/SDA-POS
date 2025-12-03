package com.DullPointers.model;

import java.util.UUID;

public class Customer implements ICustomer {
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

    @Override
    public void addLoyaltyPoints(int points) { this.loyaltyPoints += points; }

    @Override
    public void consumeLoyaltyPoints(int points) {
        if (points > this.loyaltyPoints) throw new IllegalArgumentException("Insufficient points");
        this.loyaltyPoints -= points;
    }

    @Override
    public String getId() { return id; }
    @Override
    public String getName() { return name; }
    @Override
    public String getPhoneNumber() { return phoneNumber; }
    @Override
    public int getLoyaltyPoints() { return loyaltyPoints; }
}