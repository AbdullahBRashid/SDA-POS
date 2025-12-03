package com.DullPointers.view;

import com.DullPointers.controller.ManagerController;
import com.DullPointers.manager.AuthManager;
import com.DullPointers.manager.NotificationManager;
import com.DullPointers.manager.LogManager;
import com.DullPointers.repository.ProductRepository;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

public class ManagerScreen {
    private final LogManager logManager;
    private final ProductRepository productRepository;
    private final AuthManager authManager;
    private final NotificationManager notificationManager;
    private final ViewNavigator navigator;

    public ManagerScreen(LogManager logManager, ProductRepository productRepository, NotificationManager notificationManager, AuthManager authManager, ViewNavigator navigator) {
        this.productRepository = productRepository;
        this.authManager = authManager;
        this.notificationManager = notificationManager;
        this.navigator = navigator;
        this.logManager = logManager;
    }

    public Parent getView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ManagerView.fxml"));
        Parent root = loader.load();

        ManagerController controller = loader.getController();
        controller.setLogManager(logManager);

        // Wiring logic: Call Manager logout, then Switch View
        Runnable logoutAction = () -> {
            authManager.logout();
            navigator.showLogin();
        };

        controller.setDependencies(logManager, productRepository, notificationManager, logoutAction);

        return root;
    }
}