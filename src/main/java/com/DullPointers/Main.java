package com.DullPointers;

import com.DullPointers.controller.AdminController;
import com.DullPointers.controller.CashierController;
import com.DullPointers.controller.LoginController;
import com.DullPointers.controller.ManagerController;
import com.DullPointers.manager.AuthManager;
import com.DullPointers.manager.InventoryManager;
import com.DullPointers.manager.SaleManager;
import com.DullPointers.repository.ProductRepository;
import com.DullPointers.repository.SaleRepository;
import com.DullPointers.repository.UserRepository;
import com.DullPointers.repository.impl.FileProductRepository;
import com.DullPointers.repository.impl.FileSaleRepository;
import com.DullPointers.repository.impl.FileUserRepository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;

    // Repositories (Data Layer)
    private UserRepository userRepo;
    private ProductRepository productRepo;
    private SaleRepository saleRepo;

    // Managers (Business Logic Layer)
    private AuthManager authManager;
    private InventoryManager inventoryManager;
    private SaleManager saleManager;

    @Override
    public void init() {
        // 1. Initialize Repositories
        // These load from JSON files immediately
        this.userRepo = new FileUserRepository();
        this.productRepo = new FileProductRepository();
        this.saleRepo = new FileSaleRepository();

        // 2. Initialize Managers with Dependency Injection
        this.authManager = new AuthManager(userRepo);
        this.inventoryManager = new InventoryManager(productRepo);
        this.saleManager = new SaleManager(saleRepo, productRepo, inventoryManager, authManager);
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        this.primaryStage.setTitle("Java POS System");

        // Start with the Login Screen
        showLoginScreen();

        this.primaryStage.show();
    }

    // --- NAVIGATION METHODS ---

    public void showLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
            Parent root = loader.load();

            LoginController controller = loader.getController();

            // Inject dependencies
            controller.setAuthManager(authManager);

            // Define Navigation Logic (Router)
            controller.setNavigator(new LoginController.ViewNavigator() {
                @Override
                public void navigateToCashierView() { showCashierScreen(); }

                @Override
                public void navigateToAdminView() { showAdminScreen(); }

                @Override
                public void navigateToManagerView() { showManagerScreen(); }
            });

            primaryStage.setScene(new Scene(root));
            primaryStage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load LoginView: " + e.getMessage());
        }
    }

    public void showCashierScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CashierView.fxml"));
            Parent root = loader.load();

            CashierController controller = loader.getController();
            controller.setManagers(saleManager, authManager);

            // Handle Logout: Go back to Login
            controller.setLogoutHandler(this::showLoginScreen);

            primaryStage.setScene(new Scene(root));
            primaryStage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAdminScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AdminView.fxml"));
            Parent root = loader.load();

            AdminController controller = loader.getController();

            // Inject UserRepo for user management
            controller.setDependencies(userRepo, this::showLoginScreen);

            primaryStage.setScene(new Scene(root));
            primaryStage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showManagerScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ManagerView.fxml"));
            Parent root = loader.load();

            ManagerController controller = loader.getController();

            // Inject ProductRepo for inventory management
            controller.setDependencies(productRepo, this::showLoginScreen);

            primaryStage.setScene(new Scene(root));
            primaryStage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}