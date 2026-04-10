package com.DullPointers.view;

import com.DullPointers.controller.ILoginController;
import com.DullPointers.controller.LoginController;
import com.DullPointers.manager.IAuthManager;
import com.DullPointers.manager.ILogManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

public class LoginScreen {
    private final IAuthManager authManager;
    private final ViewNavigator navigator;
    private final ILogManager logManager;

    public LoginScreen(ILogManager logManager, IAuthManager authManager, ViewNavigator navigator) {
        this.authManager = authManager;
        this.navigator = navigator;
        this.logManager = logManager;
    }

    public Parent getView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
        Parent root = loader.load();

        ILoginController controller = loader.getController();
        controller.setAuthManager(authManager);
        controller.setLogManager(logManager);

        // Map the controller's simple navigator interface to our main ViewNavigator
        controller.setNavigator(new LoginController.ViewNavigator() {
            @Override public void navigateToCashierView() { navigator.showCashier(); }
            @Override public void navigateToAdminView() { navigator.showAdmin(); }
            @Override public void navigateToManagerView() { navigator.showManager(); }
        });

        return root;
    }
}