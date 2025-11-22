package com.DullPointers.repository.impl;

import com.DullPointers.model.User;
import com.DullPointers.model.enums.Role;
import com.DullPointers.repository.UserRepository;
import com.DullPointers.util.JsonDataStore;
import com.DullPointers.util.PasswordUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileUserRepository implements UserRepository {
    // Change in .gitignore too if file name or path changes.
    private static final String FILE_PATH = "users.json";
    private List<User> database;

    public FileUserRepository() {
        // 1. Load existing users
        this.database = JsonDataStore.load(FILE_PATH, User[].class);

        // 2. SEEDING: If no users exist (first run), create default accounts
        if (database.isEmpty()) {
            System.out.println("First run detected: Creating default users...");

            // Add Admin
            database.add(new User("admin", PasswordUtil.hash("1234"), Role.ADMIN));

            // Add Cashier
            database.add(new User("cashier", PasswordUtil.hash("1234"), Role.CASHIER));

            // Add Manager
            database.add(new User("manager", PasswordUtil.hash("1234"), Role.MANAGER));

            // Save these to the file immediately
            saveToFile();
        }
    }

    private void saveToFile() {
        JsonDataStore.save(database, FILE_PATH);
    }

    @Override
    public List<User> findAll() {
        // Return a copy of the list to prevent external modification of the cache directly
        return new ArrayList<>(database);
    }

    @Override
    public void save(User user) {
        // Remove existing user with same username to handle updates
        database.removeIf(u -> u.getUsername().equals(user.getUsername()));
        database.add(user);
        saveToFile();
    }

    @Override
    public void delete(User user) {
        database.removeIf(u -> u.getUsername().equals(user.getUsername()));
        saveToFile();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return database.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }
}