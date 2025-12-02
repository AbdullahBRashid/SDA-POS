package com.DullPointers.view;

import com.DullPointers.controller.CashierController;
import com.DullPointers.manager.AuthManager;
import com.DullPointers.manager.SaleManager;
import com.DullPointers.repository.CustomerRepository;
import com.DullPointers.repository.SaleRepository;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

public class CashierScreen {
    private final SaleManager saleManager;
    private final AuthManager authManager;
    private final CustomerRepository customerRepository;
    private final SaleRepository saleRepository;
    private final ViewNavigator navigator;

    public CashierScreen(SaleManager saleManager, AuthManager authManager, CustomerRepository customerRepository, SaleRepository saleRepository, ViewNavigator navigator) {
        this.saleManager = saleManager;
        this.authManager = authManager;
        this.customerRepository = customerRepository;
        this.saleRepository = saleRepository;
        this.navigator = navigator;
    }

    public Parent getView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CashierView.fxml"));
        Parent root = loader.load();

        CashierController controller = loader.getController();
        controller.setManagers(saleManager, authManager,  customerRepository, saleRepository);

        // HERE IS THE LOGIC LINK:
        // The Controller triggers the handler -> We call AuthManager.logout() -> We navigate
        controller.setLogoutHandler(() -> {
            authManager.logout(); // Records the Shift End Time & Saves to JSON
            navigator.showLogin();
        });

        return root;
    }
}