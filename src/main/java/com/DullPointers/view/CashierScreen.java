package com.DullPointers.view;

import com.DullPointers.controller.ICashierController;
import com.DullPointers.manager.IAuthManager;
import com.DullPointers.manager.ILogManager;
import com.DullPointers.manager.ISaleManager;
import com.DullPointers.repository.CustomerRepository;
import com.DullPointers.repository.SaleRepository;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

public class CashierScreen {
    private final ILogManager logManager;
    private final ISaleManager saleManager;
    private final IAuthManager authManager;
    private final CustomerRepository customerRepository;
    private final SaleRepository saleRepository;
    private final ViewNavigator navigator;

    public CashierScreen(ILogManager logManager, ISaleManager saleManager, IAuthManager authManager, CustomerRepository customerRepository, SaleRepository saleRepository, ViewNavigator navigator) {
        this.saleManager = saleManager;
        this.authManager = authManager;
        this.customerRepository = customerRepository;
        this.saleRepository = saleRepository;
        this.navigator = navigator;
        this.logManager = logManager;
    }

    public Parent getView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CashierView.fxml"));
        Parent root = loader.load();

        ICashierController controller = loader.getController();
        controller.setManagers(logManager, saleManager, authManager,  customerRepository, saleRepository);

        // HERE IS THE LOGIC LINK:
        // The Controller triggers the handler -> We call AuthManager.logout() -> We navigate
        controller.setLogoutHandler(() -> {
            authManager.logout(); // Records the Shift End Time & Saves to JSON
            navigator.showLogin();
        });

        return root;
    }
}