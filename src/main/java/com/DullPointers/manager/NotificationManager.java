package com.DullPointers.manager;

import java.util.ArrayList;
import java.util.List;

public class NotificationManager {
    private final List<String> alerts = new ArrayList<>();

    public void addAlert(String message) {
        alerts.add(message);
    }

    public List<String> getAlerts() {
        return new ArrayList<>(alerts);
    }

    public void clearAlerts() {
        alerts.clear();
    }
}