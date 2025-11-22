package com.DullPointers;

import com.DullPointers.manager.AuthManager;
import com.DullPointers.manager.InventoryManager;
import com.DullPointers.manager.SaleManager;
import com.DullPointers.repository.impl.FileProductRepository;
import com.DullPointers.repository.impl.FileSaleRepository;
import com.DullPointers.repository.impl.FileUserRepository;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    // Define Managers as fields so the UI can use them later
    private SaleManager saleManager;
    private AuthManager authManager;
    private InventoryManager inventoryManager;

    @Override
    public void init() {
        // 1. Create Repositories (The Data Layer)
        // These now load from .json files instead of memory
        var productRepo = new FileProductRepository();
        var saleRepo = new FileSaleRepository();
        var userRepo = new FileUserRepository();

        // 2. Create Managers (The Business Logic Layer)
        // We inject the repositories into the managers (Dependency Injection)
        this.authManager = new AuthManager(userRepo);
        this.inventoryManager = new InventoryManager(productRepo);

        // SaleManager needs access to Products (prices), Inventory (stock), and Auth (current user)
        this.saleManager = new SaleManager(saleRepo, productRepo, inventoryManager, authManager);

        // 3. Login automatically for testing purposes
        // In the final app, you will remove this and show a Login Screen instead
        try {
            authManager.authenticate("admin", "1234");
            System.out.println("System initialized. Admin logged in.");
        } catch (SecurityException e) {
            System.err.println("Auto-login failed: " + e.getMessage());
        }
    }

    @Override
    public void start(Stage stage) {
        // 4. Create a simple temporary GUI to test the backend
        Label statusLabel = new Label("Ready to Sell");
        Button sellBtn = new Button("Test Sale: Sell 1 Coke");

        sellBtn.setOnAction(e -> {
            try {
                // Simulate scanning a barcode
                saleManager.addItemToSale("111", 1);

                // Update UI with the calculation result
                statusLabel.setText("Item added! Cart Total: $" + saleManager.getCurrentSale().calculateGrandTotal());

                // Optional: Complete the sale immediately to test file saving
                // saleManager.completeSale();

            } catch (Exception ex) {
                statusLabel.setText("Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        // Simple Layout
        VBox root = new VBox(20, statusLabel, sellBtn);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 400, 300);
        stage.setTitle("POS System - Backend Test");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}