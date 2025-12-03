package com.DullPointers.model;

import com.DullPointers.model.enums.Role;

public interface IUser {
    String getPasswordHash();

    // Logic: Verify permission
    boolean isAdmin();

    String getUsername();

    Role getRole();

    boolean verifyPassword(String inputHash);
}
