package com.DullPointers.view;

import com.DullPointers.controller.AdminController;
import com.DullPointers.manager.AuthManager;
import com.DullPointers.manager.LogManager;
import com.DullPointers.repository.UserRepository;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

public class AdminScreen {
    private final LogManager logManager;
    private final UserRepository userRepository;
    private final AuthManager authManager;
    private final ViewNavigator navigator;

    public AdminScreen(LogManager logManager, UserRepository userRepository, AuthManager authManager, ViewNavigator navigator) {
        this.userRepository = userRepository;
        this.authManager = authManager;
        this.navigator = navigator;
        this.logManager = logManager;
    }

    public Parent getView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AdminView.fxml"));
        Parent root = loader.load();

        AdminController controller = loader.getController();
        controller.setLogManager(this.logManager);

        // Wiring logic: Call Manager logout, then Switch View
        Runnable logoutAction = () -> {
            authManager.logout();
            navigator.showLogin();
        };

        controller.setDependencies(userRepository, logoutAction);

        return root;
    }
}