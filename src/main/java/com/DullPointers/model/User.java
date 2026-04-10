package com.DullPointers.model;

import com.DullPointers.model.enums.Role;

public class User implements IUser {
    private String username;
    private String passwordHash; // Req 14
    private String fullName;
    private Role role; // Req 13

    public User() {}

    public User(String username, String passwordHash, Role role) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    @Override
    public String getPasswordHash() {
        return passwordHash;
    }

    // Logic: Verify permission
    @Override
    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }

    @Override
    public String getUsername() { return username; }
    @Override
    public Role getRole() { return role; }
    @Override
    public boolean verifyPassword(String inputHash) {
        return this.passwordHash.equals(inputHash);
    }
}