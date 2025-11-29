package com.DullPointers.view;

import com.DullPointers.controller.ManagerController;
import com.DullPointers.manager.AuthManager;
import com.DullPointers.repository.ProductRepository;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

public class ManagerScreen {
    private final ProductRepository productRepository;
    private final AuthManager authManager;
    private final ViewNavigator navigator;

    public ManagerScreen(ProductRepository productRepository, AuthManager authManager, ViewNavigator navigator) {
        this.productRepository = productRepository;
        this.authManager = authManager;
        this.navigator = navigator;
    }

    public Parent getView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("//view/ManagerView.fxml"));
        Parent root = loader.load();

        ManagerController controller = loader.getController();

        // Wiring logic: Call Manager logout, then Switch View
        Runnable logoutAction = () -> {
            authManager.logout();
            navigator.showLogin();
        };

        controller.setDependencies(productRepository, logoutAction);

        return root;
    }
}