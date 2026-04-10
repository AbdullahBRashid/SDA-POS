package com.DullPointers.manager;

import com.DullPointers.model.IUser;

public interface IAuthManager {
    // Logic for Req 14: Authentication
    IUser authenticate(String username, String pin) throws SecurityException;

    void logout();

    IUser getCurrentUser();
}
