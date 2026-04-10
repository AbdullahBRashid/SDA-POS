package com.DullPointers.manager;

import java.util.List;

public interface INotificationManager {
    void addAlert(String message);

    List<String> getAlerts();

    void clearAlerts();
}
