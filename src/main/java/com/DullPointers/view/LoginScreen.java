package com.DullPointers.view;

import com.DullPointers.controller.LoginController;
import com.DullPointers.manager.AuthManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

public class LoginScreen {
    private final AuthManager authManager;
    private final ViewNavigator navigator;

    public LoginScreen(AuthManager authManager, ViewNavigator navigator) {
        this.authManager = authManager;
        this.navigator = navigator;
    }

    public Parent getView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
        Parent root = loader.load();

        LoginController controller = loader.getController();
        controller.setAuthManager(authManager);

        // Map the controller's simple navigator interface to our main ViewNavigator
        controller.setNavigator(new LoginController.ViewNavigator() {
            @Override public void navigateToCashierView() { navigator.showCashier(); }
            @Override public void navigateToAdminView() { navigator.showAdmin(); }
            @Override public void navigateToManagerView() { navigator.showManager(); }
        });

        return root;
    }
}