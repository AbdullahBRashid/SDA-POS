package com.DullPointers.controller;

import com.DullPointers.manager.INotificationManager;
import com.DullPointers.manager.ILogManager;
import com.DullPointers.model.IProduct;
import com.DullPointers.model.Product;
import com.DullPointers.repository.ProductRepository;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;

public class ManagerController implements IManagerController {

    @FXML private TableView<IProduct> productTable;
    @FXML private TableColumn<IProduct, String> colBarcode;
    @FXML private TableColumn<IProduct, String> colName;
    @FXML private TableColumn<IProduct, Integer> colStock;

    @FXML private TextField barcodeField;
    @FXML private TextField nameField;
    @FXML private TextField priceField;
    @FXML private TextField stockField;
    @FXML private Label msgLabel;

    @FXML private ListView<String> notificationList; // NEW

    private ILogManager logManager;
    private ProductRepository productRepository;
    private INotificationManager notificationManager;
    private Runnable logoutHandler;

    @FXML
    @Override
    public void initialize() {
        // 1. Existing Column Setup
        colBarcode.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getBarcode()));
        colName.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));
        colStock.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getStockQuantity()));

        // 2. NEW: Add Selection Listener
        productTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateForm(newSelection);
            }
        });
    }

    private void populateForm(IProduct product) {
        barcodeField.setText(product.getBarcode());
        nameField.setText(product.getName());

        // Handle BigDecimal conversion for price
        if (product.getSellingPrice() != null) {
            priceField.setText(product.getSellingPrice().toString());
        } else {
            priceField.clear();
        }

        stockField.setText(String.valueOf(product.getStockQuantity()));

        // Optional: Give visual feedback that we are in "Edit Mode"
        msgLabel.setText("Editing: " + product.getName());
        msgLabel.setStyle("-fx-text-fill: blue;");
    }

    @Override
    public void setDependencies(ILogManager logman, ProductRepository repo, INotificationManager notifMgr, Runnable logout) {
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
            IProduct product = new Product(barcode, name, price, stock);

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
        msgLabel.setText(""); // Clear status message

        // Deselect the row so the user knows they are creating a NEW entry
        productTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleRefresh() {
        loadProducts();
    }

    @Override
    public void setLogManager(ILogManager logManager) {
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