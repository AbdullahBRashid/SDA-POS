package com.DullPointers.view;

import com.DullPointers.controller.IAdminController;
import com.DullPointers.manager.IAuthManager;
import com.DullPointers.manager.ILogManager;
import com.DullPointers.repository.UserRepository;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

public class AdminScreen {
    private final ILogManager logManager;
    private final UserRepository userRepository;
    private final IAuthManager authManager;
    private final ViewNavigator navigator;

    public AdminScreen(ILogManager logManager, UserRepository userRepository, IAuthManager authManager, ViewNavigator navigator) {
        this.userRepository = userRepository;
        this.authManager = authManager;
        this.navigator = navigator;
        this.logManager = logManager;
    }

    public Parent getView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AdminView.fxml"));
        Parent root = loader.load();

        IAdminController controller = loader.getController();
        controller.setLogManager(this.logManager);

        // Wiring logic: Call Manager logout, then Switch View
        Runnable logoutAction = () -> {
            authManager.logout();
            navigator.showLogin();
        };

        controller.setDependencies(userRepository, logoutAction);
        controller.setAuthManager(authManager);

        return root;
    }
}