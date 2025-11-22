package com.DullPointers.model;

import com.DullPointers.model.enums.Role;

public class User {
    private String username;
    private String passwordHash; // Req 14
    private String fullName;
    private Role role; // Req 13

    public User(String username, String passwordHash, Role role) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    // Logic: Verify permission
    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }

    public String getUsername() { return username; }
    public Role getRole() { return role; }
    public boolean verifyPassword(String inputHash) {
        return this.passwordHash.equals(inputHash);
    }
}