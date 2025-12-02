package com.DullPointers;

import com.DullPointers.manager.*;
import com.DullPointers.repository.*;
import com.DullPointers.repository.impl.*;
import com.DullPointers.view.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application implements ViewNavigator {
    private Stage primaryStage;

    // Core Dependencies
    private UserRepository userRepo;
    private ProductRepository productRepo;
    private SaleRepository saleRepo;
    private ShiftRepository shiftRepo;
    private CustomerRepository customerRepo;

    private AuthManager authManager;
    private InventoryManager inventoryManager;
    private SaleManager saleManager;
    private NotificationManager notificationManager;

    @Override
    public void init() {
        // 1. Data Layer
        this.userRepo = new FileUserRepository();
        this.productRepo = new FileProductRepository();
        this.saleRepo = new FileSaleRepository();
        this.shiftRepo = new FileShiftRepository();
        this.customerRepo = new FileCustomerRepository();

        // 2. Business Layer
        this.notificationManager = new NotificationManager();
        this.inventoryManager = new InventoryManager(productRepo, notificationManager);
        this.authManager = new AuthManager(userRepo, shiftRepo);
        this.saleManager = new SaleManager(saleRepo, productRepo, inventoryManager, authManager, customerRepo);
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        showLogin();
        stage.show();
    }

    // --- NAVIGATION ---
    @Override
    public void showLogin() {
        try {
            LoginScreen s = new LoginScreen(authManager, this);
            primaryStage.setScene(new Scene(s.getView()));
        } catch(Exception e) { e.printStackTrace(); }
    }

    @Override
    public void showCashier() {
        try {
            // Updated to pass the repositories
            CashierScreen screen = new CashierScreen(
                    saleManager,
                    authManager,
                    customerRepo,
                    saleRepo,
                    this
            );

            primaryStage.setScene(new Scene(screen.getView()));
            primaryStage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showManager() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ManagerView.fxml"));
            Parent root = loader.load();
            com.DullPointers.controller.ManagerController c = loader.getController();

            c.setDependencies(productRepo, notificationManager, () -> { authManager.logout(); showLogin(); });

            primaryStage.setScene(new Scene(root));
        } catch(Exception e) { e.printStackTrace(); }
    }

    @Override
    public void showAdmin() {
        try {
            AdminScreen s = new AdminScreen(userRepo, authManager, this);
            primaryStage.setScene(new Scene(s.getView()));
        } catch(Exception e) { e.printStackTrace(); }
    }

    public static void main(String[] args) { launch(args); }
}