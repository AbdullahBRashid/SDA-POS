package com.DullPointers.controller;

import com.DullPointers.manager.ILogManager;
import com.DullPointers.manager.INotificationManager;
import com.DullPointers.repository.ProductRepository;
import com.DullPointers.repository.SaleRepository;
import javafx.fxml.FXML;

public interface IManagerController {
    @FXML
    void initialize();

    void setDependencies(SaleRepository saleRepository, ILogManager logman, ProductRepository repo, INotificationManager notifMgr, Runnable logout);

    void setLogManager(ILogManager logManager);
}
