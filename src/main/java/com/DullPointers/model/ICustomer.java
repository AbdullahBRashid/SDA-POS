package com.DullPointers.model;

public interface ICustomer {
    void addLoyaltyPoints(int points);

    void consumeLoyaltyPoints(int points);

    String getId();

    String getName();

    String getPhoneNumber();

    int getLoyaltyPoints();
}
