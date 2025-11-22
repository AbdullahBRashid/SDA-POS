package com.DullPointers.controller;

import com.DullPointers.manager.AuthManager;
import com.DullPointers.model.User;
import com.DullPointers.model.enums.Role;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField pinField;
    @FXML private Label errorLabel;
    @FXML private Button loginButton;

    private AuthManager authManager;
    // Interface for view navigation (DIP) - implemented by main application
    private ViewNavigator navigator;

    /**
     * Dependency Injection setter for AuthManager.
     * This ensures the Controller doesn't tightly couple to the creation of Manager.
     */
    public void setAuthManager(AuthManager authManager) {
        this.authManager = authManager;
    }

    public void setNavigator(ViewNavigator navigator) {
        this.navigator = navigator;
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String pin = pinField.getText();

        if (username.isEmpty() || pin.isEmpty()) {
            showError("Please enter both username and PIN.");
            return;
        }

        try {
            User user = authManager.authenticate(username, pin);

            // clear fields for security
            pinField.clear();
            errorLabel.setVisible(false);

            // Route based on Role (Open/Closed Principle applied via strategy or simple switch here)
            routeUser(user);

        } catch (SecurityException e) {
            showError("Invalid Login: " + e.getMessage());
        } catch (Exception e) {
            showError("System Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void routeUser(User user) {
        if (navigator == null) {
            System.err.println("Navigator not set. Cannot route user.");
            return;
        }

        Role role = user.getRole();
        switch (role) {
            case CASHIER:
                navigator.navigateToCashierView();
                break;
            case ADMIN:
                navigator.navigateToAdminView();
                break;
            case MANAGER:
                navigator.navigateToManagerView();
                break;
            default:
                showError("User has no assigned role.");
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    // Simple interface to decouple navigation logic from the controller
    public interface ViewNavigator {
        void navigateToCashierView();
        void navigateToAdminView();
        void navigateToManagerView();
    }
}