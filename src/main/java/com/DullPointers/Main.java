package com.DullPointers;

import com.DullPointers.controller.IManagerController;
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
    private CustomerRepository customerRepo;

    // Managers (Business Logic Layer)
    private IAuthManager authManager;
    private IInventoryManager inventoryManager;
    private ISaleManager saleManager;
    private ILogManager logManager;
    private INotificationManager notificationManager;

    @Override
    public void init() {
        // 1. Data Layer
        this.userRepo = new FileUserRepository();
        this.productRepo = new FileProductRepository();
        this.saleRepo = new FileSaleRepository();
        ShiftRepository shiftRepo = new FileShiftRepository();
        this.customerRepo = new FileCustomerRepository();
        LogRepository logRepo = new FileLogRepository();

        // 2. Business Layer
        this.notificationManager = new NotificationManager();
        this.inventoryManager = new InventoryManager(productRepo, notificationManager);
        this.logManager = new LogManager(logRepo);
        this.authManager = new AuthManager(userRepo, shiftRepo);
        this.saleManager = new SaleManager(authManager, saleRepo, productRepo, inventoryManager, customerRepo);
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        this.primaryStage.setTitle("Java POS System");

        // Start the app at the Login Screen
        showLogin();
        stage.show();
    }

    // --- VIEW NAVIGATOR IMPLEMENTATION ---
    // These methods allow your screens to switch views without knowing the details

    @Override
    public void showLogin() {
        try {
            LoginScreen s = new LoginScreen(logManager, authManager, this);
            primaryStage.setScene(new Scene(s.getView()));
        } catch(Exception e) { e.printStackTrace(); }
    }

    @Override
    public void showCashier() {
        try {
            // Updated to pass the repositories
            CashierScreen screen = new CashierScreen(
                    logManager,
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
            IManagerController c = loader.getController();

            c.setDependencies(logManager, productRepo, notificationManager, () -> { authManager.logout(); showLogin(); });

            primaryStage.setScene(new Scene(root));
        } catch(Exception e) { e.printStackTrace(); }
    }

    @Override
    public void showAdmin() {
        try {
            AdminScreen s = new AdminScreen(logManager, userRepo, authManager, this);
            primaryStage.setScene(new Scene(s.getView()));
        } catch(Exception e) { e.printStackTrace(); }
    }

    public static void main(String[] args) { launch(args); }
}