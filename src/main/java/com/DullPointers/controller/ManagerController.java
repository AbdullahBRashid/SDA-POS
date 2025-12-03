package com.DullPointers.controller;

import com.DullPointers.manager.NotificationManager;
import com.DullPointers.manager.LogManager;
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

    @FXML private TextField barcodeField;
    @FXML private TextField nameField;
    @FXML private TextField priceField;
    @FXML private TextField stockField;
    @FXML private Label msgLabel;

    @FXML private ListView<String> notificationList; // NEW

    private LogManager logManager;
    private ProductRepository productRepository;
    private NotificationManager notificationManager;
    private Runnable logoutHandler;

    @FXML
    public void initialize() {
        colBarcode.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getBarcode()));
        colName.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));
        colStock.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getStockQuantity()));
    }

    public void setDependencies(LogManager logman, ProductRepository repo, NotificationManager notifMgr, Runnable logout) {
        this.logManager = logman;
        this.productRepository = repo;
        this.notificationManager = notifMgr;
        this.logoutHandler = logout;
        loadProducts();
        loadNotifications();
    }

    @FXML
    private void handleSaveProduct() {
        try {
            String barcode = barcodeField.getText();
            String name = nameField.getText();
            BigDecimal price = new BigDecimal(priceField.getText());
            int stock = Integer.parseInt(stockField.getText());

            // Create new product object
            Product product = new Product(barcode, name, price, stock);

            // Save to repo (Assumes your ProductRepository has a save method)
            productRepository.save(product);

            msgLabel.setText("Saved: " + name);
            msgLabel.setStyle("-fx-text-fill: green;");

            loadProducts(); // Refresh table
            handleClear();  // Reset form

        } catch (NumberFormatException e) {
            msgLabel.setText("Invalid Price or Stock format.");
            msgLabel.setStyle("-fx-text-fill: red;");
        } catch (Exception e) {
            msgLabel.setText("Error: " + e.getMessage());
            msgLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void handleClear() {
        barcodeField.clear();
        nameField.clear();
        priceField.clear();
        stockField.clear();
        productTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleRefresh() {
        loadProducts();
    }

    public void setLogManager(LogManager logManager) {
        this.logManager = logManager;
    }

    @FXML
    private void handleLogout() {
        if (logoutHandler != null) logoutHandler.run();
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
}