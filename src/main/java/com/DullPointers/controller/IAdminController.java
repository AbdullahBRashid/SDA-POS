package com.DullPointers.controller;

import com.DullPointers.manager.IAuthManager;
import com.DullPointers.manager.ILogManager;
import com.DullPointers.repository.UserRepository;
import javafx.fxml.FXML;

public interface IAdminController {
    // --- Initialization ---
    @FXML
    void initialize();

    void setDependencies(UserRepository userRepository, Runnable logoutHandler);

    void setLogManager(ILogManager logManager);

    void setAuthManager(IAuthManager authManager);
}
