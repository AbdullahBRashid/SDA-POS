package com.DullPointers.controller;

import com.DullPointers.manager.IAuthManager;
import com.DullPointers.manager.ILogManager;

public interface ILoginController {
    void setAuthManager(IAuthManager authManager);

    void setLogManager(ILogManager logManager);

    void setNavigator(ViewNavigator navigator);

    // Simple interface to decouple navigation logic from the controller
    public interface ViewNavigator {
        void navigateToCashierView();

        void navigateToAdminView();

        void navigateToManagerView();
    }
}
