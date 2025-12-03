package com.DullPointers.controller;

import com.DullPointers.manager.IAuthManager;
import com.DullPointers.manager.ILogManager;
import com.DullPointers.manager.ISaleManager;
import com.DullPointers.repository.CustomerRepository;
import com.DullPointers.repository.SaleRepository;
import javafx.fxml.FXML;

public interface ICashierController {
    // --- Initialization ---
    @FXML
    void initialize();

    void setManagers(ILogManager logManager, ISaleManager saleManager, IAuthManager authManager,
                     CustomerRepository customerRepo, SaleRepository saleRepo);

    void setLogoutHandler(Runnable handler);
}
