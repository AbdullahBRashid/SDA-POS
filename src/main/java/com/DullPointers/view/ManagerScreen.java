package com.DullPointers.view;

import com.DullPointers.controller.IManagerController;
import com.DullPointers.manager.IAuthManager;
import com.DullPointers.manager.INotificationManager;
import com.DullPointers.manager.ILogManager;
import com.DullPointers.repository.ProductRepository;
import com.DullPointers.repository.SaleRepository;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

public class ManagerScreen {
    private final ILogManager logManager;
    private final ProductRepository productRepository;
    private final IAuthManager authManager;
    private final INotificationManager notificationManager;
    private final SaleRepository salerepository;
    private final ViewNavigator navigator;

    public ManagerScreen(SaleRepository salerepository, ILogManager logManager, ProductRepository productRepository, INotificationManager notificationManager, IAuthManager authManager, ViewNavigator navigator) {
        this.productRepository = productRepository;
        this.authManager = authManager;
        this.notificationManager = notificationManager;
        this.navigator = navigator;
        this.logManager = logManager;
        this.salerepository = salerepository;
    }

    public Parent getView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ManagerView.fxml"));
        Parent root = loader.load();

        IManagerController controller = loader.getController();
        controller.setLogManager(logManager);

        // Wiring logic: Call Manager logout, then Switch View
        Runnable logoutAction = () -> {
            authManager.logout();
            navigator.showLogin();
        };

        controller.setDependencies(salerepository, logManager, productRepository, notificationManager, logoutAction);

        return root;
    }
}