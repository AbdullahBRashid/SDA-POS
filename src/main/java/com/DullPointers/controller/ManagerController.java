package com.DullPointers.controller;

import com.DullPointers.manager.NotificationManager;
import com.DullPointers.model.Product;
import com.DullPointers.repository.ProductRepository;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;

public class ManagerController {
    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, String> colBarcode;
    @FXML private TableColumn<Product, String> colName;
    @FXML private TableColumn<Product, Integer> colStock;

    @FXML private ListView<String> notificationList; // NEW

    private ProductRepository productRepository;
    private NotificationManager notificationManager;
    private Runnable logoutHandler;

    @FXML
    public void initialize() {
        colBarcode.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getBarcode()));
        colName.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));
        colStock.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getStockQuantity()));
    }

    public void setDependencies(ProductRepository repo, NotificationManager notifMgr, Runnable logout) {
        this.productRepository = repo;
        this.notificationManager = notifMgr;
        this.logoutHandler = logout;
        loadProducts();
        loadNotifications();
    }

    private void loadProducts() {
        if(productRepository != null) productTable.setItems(FXCollections.observableArrayList(productRepository.findAll()));
    }

    private void loadNotifications() {
        if(notificationManager != null) notificationList.getItems().setAll(notificationManager.getAlerts());
    }

    @FXML private void handleClearNotifications() {
        if(notificationManager != null) notificationManager.clearAlerts();
        loadNotifications();
    }

    @FXML private void handleRefresh() { loadProducts(); loadNotifications(); }
    @FXML private void handleLogout() { if(logoutHandler != null) logoutHandler.run(); }
}