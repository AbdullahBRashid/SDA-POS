package com.DullPointers.controller;

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
    @FXML private TableColumn<Product, BigDecimal> colPrice;
    @FXML private TableColumn<Product, Integer> colStock;

    @FXML private TextField barcodeField;
    @FXML private TextField nameField;
    @FXML private TextField priceField;
    @FXML private TextField stockField;
    @FXML private Label msgLabel;

    private ProductRepository productRepository;
    private Runnable logoutHandler;

    @FXML
    public void initialize() {
        colBarcode.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getBarcode()));
        colName.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));
        colPrice.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getSellingPrice()));
        colStock.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getStockQuantity()));

        // Optional: Add listener to table selection to populate form for editing
        productTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) populateForm(newVal);
        });
    }

    public void setDependencies(ProductRepository productRepository, Runnable logoutHandler) {
        this.productRepository = productRepository;
        this.logoutHandler = logoutHandler;
        loadProducts();
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
    private void handleRefresh() {
        loadProducts();
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
    private void handleLogout() {
        if (logoutHandler != null) logoutHandler.run();
    }

    private void loadProducts() {
        if (productRepository == null) return;
        try {
            // Assumes productRepository.findAll() exists and returns List<Product>
            productTable.setItems(FXCollections.observableArrayList(productRepository.findAll()));
        } catch (Exception e) {
            msgLabel.setText("Could not load products.");
        }
    }

    private void populateForm(Product p) {
        barcodeField.setText(p.getBarcode());
        nameField.setText(p.getName());
        priceField.setText(p.getSellingPrice().toString());
        stockField.setText(String.valueOf(p.getStockQuantity()));
    }
}