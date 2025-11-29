package com.DullPointers;

import com.DullPointers.manager.AuthManager;
import com.DullPointers.manager.InventoryManager;
import com.DullPointers.manager.SaleManager;

// Interfaces
import com.DullPointers.repository.ProductRepository;
import com.DullPointers.repository.SaleRepository;
import com.DullPointers.repository.ShiftRepository;
import com.DullPointers.repository.UserRepository;

// Implementations (The File-based databases)
import com.DullPointers.repository.impl.FileProductRepository;
import com.DullPointers.repository.impl.FileSaleRepository;
import com.DullPointers.repository.impl.FileShiftRepository;
import com.DullPointers.repository.impl.FileUserRepository;

// View Classes (The separate screen loaders you created)
import com.DullPointers.view.AdminScreen;
import com.DullPointers.view.CashierScreen;
import com.DullPointers.view.LoginScreen;
import com.DullPointers.view.ManagerScreen;
import com.DullPointers.view.ViewNavigator;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application implements ViewNavigator {

    private Stage primaryStage;

    // Repositories (Data Layer)
    private UserRepository userRepo;
    private ProductRepository productRepo;
    private SaleRepository saleRepo;
    private ShiftRepository shiftRepo;

    // Managers (Business Logic Layer)
    private AuthManager authManager;
    private InventoryManager inventoryManager;
    private SaleManager saleManager;

    @Override
    public void init() {
        // 1. Initialize Repositories (Load from JSON files)
        this.userRepo = new FileUserRepository();
        this.productRepo = new FileProductRepository();
        this.saleRepo = new FileSaleRepository();
        this.shiftRepo = new FileShiftRepository();

        // 2. Initialize Managers (Dependency Injection)
        // AuthManager needs ShiftRepo to track user shifts
        this.authManager = new AuthManager(userRepo, shiftRepo);
        this.inventoryManager = new InventoryManager(productRepo);

        // SaleManager needs access to sales, products, inventory checks, and the current user
        this.saleManager = new SaleManager(saleRepo, productRepo, inventoryManager, authManager);
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        this.primaryStage.setTitle("Java POS System");

        // Start the app at the Login Screen
        showLogin();

        this.primaryStage.show();
    }

    // --- VIEW NAVIGATOR IMPLEMENTATION ---
    // These methods allow your screens to switch views without knowing the details

    @Override
    public void showLogin() {
        try {
            LoginScreen screen = new LoginScreen(authManager, this);
            primaryStage.setScene(new Scene(screen.getView()));
            primaryStage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Critical Error: Could not load Login View.");
        }
    }

    @Override
    public void showCashier() {
        try {
            CashierScreen screen = new CashierScreen(saleManager, authManager, this);
            primaryStage.setScene(new Scene(screen.getView()));
            primaryStage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showAdmin() {
        try {
            AdminScreen screen = new AdminScreen(userRepo, authManager, this);
            primaryStage.setScene(new Scene(screen.getView()));
            primaryStage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showManager() {
        try {
            ManagerScreen screen = new ManagerScreen(productRepo, authManager, this);
            primaryStage.setScene(new Scene(screen.getView()));
            primaryStage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}