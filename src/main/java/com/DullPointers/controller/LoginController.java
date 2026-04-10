package com.DullPointers.controller;

import com.DullPointers.manager.IAuthManager;
import com.DullPointers.manager.ILogManager;
import com.DullPointers.model.IUser;
import com.DullPointers.model.enums.LogType;
import com.DullPointers.model.enums.Role;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController implements ILoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField pinField;
    @FXML private Label errorLabel;
    @FXML private Button loginButton;

    private IAuthManager authManager;
    private ILogManager logManager;
    // Interface for view navigation (DIP) - implemented by main application
    private ViewNavigator navigator;

    /**
     * Dependency Injection setter for AuthManager.
     * This ensures the Controller doesn't tightly couple to the creation of Manager.
     */
    @Override
    public void setAuthManager(IAuthManager authManager) {
        this.authManager = authManager;
    }

    @Override
    public void setLogManager(ILogManager logManager) {
        this.logManager = logManager;
    }

    @Override
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
            IUser user = authManager.authenticate(username, pin);

            // clear fields for security
            pinField.clear();
            errorLabel.setVisible(false);

            logManager.saveLog(LogType.SUCCESS, "User %s logged in successfully!".formatted(username));

            // Route based on Role (Open/Closed Principle applied via strategy or simple switch here)
            routeUser(user);

        } catch (SecurityException e) {
            showError("Invalid Login: " + e.getMessage());
            logManager.saveLog(LogType.ERROR, "Invalid Login: " + e.getMessage());

        } catch (Exception e) {
            showError("System Error: " + e.getMessage());
            logManager.saveLog(LogType.ERROR, "System Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void routeUser(IUser user) {
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

}