package com.DullPointers.manager;

import java.util.ArrayList;
import java.util.List;

public class NotificationManager implements INotificationManager {
    private final List<String> alerts = new ArrayList<>();

    @Override
    public void addAlert(String message) {
        alerts.add(message);
    }

    @Override
    public List<String> getAlerts() {
        return new ArrayList<>(alerts);
    }

    @Override
    public void clearAlerts() {
        alerts.clear();
    }
}