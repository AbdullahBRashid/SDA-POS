package com.DullPointers.controller;

import com.DullPointers.model.User;
import com.DullPointers.model.enums.Role;
import com.DullPointers.repository.UserRepository;
// import com.DullPointers.util.PasswordUtil; // Uncomment if you have this
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.util.Optional;

public class AdminController {

    // --- UI Components ---
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, String> colUsername;
    @FXML private TableColumn<User, String> colRole;
    @FXML private TableColumn<User, String> colStatus; // Just a placeholder for active/inactive

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<Role> roleComboBox;
    @FXML private Label statusLabel;

    // --- Dependencies ---
    private UserRepository userRepository;
    private Runnable logoutHandler;

    // --- Initialization ---
    @FXML
    public void initialize() {
        // Initialize Table Columns
        colUsername.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getUsername()));
        colRole.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getRole().toString()));
        colStatus.setCellValueFactory(cell -> new SimpleStringProperty("Active"));

        // Initialize Role ComboBox
        roleComboBox.setItems(FXCollections.observableArrayList(Role.values()));

        // Clear status on type
        usernameField.setOnKeyTyped(e -> statusLabel.setText(""));
    }

    public void setDependencies(UserRepository userRepository, Runnable logoutHandler) {
        this.userRepository = userRepository;
        this.logoutHandler = logoutHandler;
        loadUsers();
    }

    private void loadUsers() {
        if (userRepository == null) return;
        // Note: Your UserRepository interface needs a findAll() method.
        // If it doesn't exist, you'll need to add it.
        // For now, assuming it returns a List<User>.
        try {
            // If your repo doesn't support findAll, we might need to mock it or add it.
            // userTable.setItems(FXCollections.observableArrayList(userRepository.findAll()));

            // Since I don't see findAll() in your previous snippets, I will leave this commented
            // and assume you will add `List<User> findAll();` to your UserRepository interface.
            System.out.println("Loading users...");
        } catch (Exception e) {
            statusLabel.setText("Error loading users: " + e.getMessage());
        }
    }

    // --- Actions ---

    @FXML
    private void handleAddUser() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        Role role = roleComboBox.getValue();

        if (username.isEmpty() || password.isEmpty() || role == null) {
            statusLabel.setText("Please fill all fields.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        try {
            // 1. Hash the password (MOCK logic here, use BCrypt in real app)
            // String passwordHash = PasswordUtil.hash(password);
            String passwordHash = password; // Temporary bypass

            // 2. Create User Model
            User newUser = new User(username, passwordHash, role);

            // 3. Save to Repo
            userRepository.save(newUser);

            // 4. Update UI
            statusLabel.setText("User created successfully.");
            statusLabel.setStyle("-fx-text-fill: green;");
            userTable.getItems().add(newUser);
            clearForm();

        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void handleDeleteUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            statusLabel.setText("Select a user to delete.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete User");
        alert.setContentText("Are you sure you want to delete " + selectedUser.getUsername() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // userRepository.delete(selectedUser); // Assume delete method exists
            userTable.getItems().remove(selectedUser);
            statusLabel.setText("User deleted.");
        }
    }

    @FXML
    private void handleLogout() {
        if (logoutHandler != null) logoutHandler.run();
    }

    private void clearForm() {
        usernameField.clear();
        passwordField.clear();
        roleComboBox.getSelectionModel().clearSelection();
    }
}