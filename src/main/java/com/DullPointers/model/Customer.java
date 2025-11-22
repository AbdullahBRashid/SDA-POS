package com.DullPointers.model;

public class Customer {
    private String phoneNumber; // Primary Search Key (Req 21)
    private String name;
    private int loyaltyPoints; // Req 19

    public Customer(String phoneNumber, String name) {
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.loyaltyPoints = 0;
    }

    public void addLoyaltyPoints(int points) {
        this.loyaltyPoints += points;
    }

    public String getPhoneNumber() { return phoneNumber; }
    public String getName() { return name; }
    public int getLoyaltyPoints() { return loyaltyPoints; }
}