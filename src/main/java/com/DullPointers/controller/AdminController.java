package com.DullPointers.controller;

import com.DullPointers.manager.IAuthManager;
import com.DullPointers.manager.ILogManager;
import com.DullPointers.model.IUser;
import com.DullPointers.model.User;
import com.DullPointers.model.enums.LogType;
import com.DullPointers.model.enums.Role;
import com.DullPointers.repository.UserRepository;
// import com.DullPointers.util.PasswordUtil;
import com.DullPointers.util.PasswordUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Optional;

public class AdminController implements IAdminController {

    // --- UI Components ---
    @FXML private TableView<IUser> userTable;
    @FXML private TableColumn<IUser, String> colUsername;
    @FXML private TableColumn<IUser, String> colRole;
    @FXML private TableColumn<IUser, String> colStatus; // Just a placeholder for active/inactive

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<Role> roleComboBox;
    @FXML private Label statusLabel;

    // --- Dependencies ---
    private IAuthManager authManager;
    private ILogManager logManager;
    private UserRepository userRepository;
    private Runnable logoutHandler;

    // --- Initialization ---
    @FXML
    @Override
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

    @Override
    public void setDependencies(UserRepository userRepository, Runnable logoutHandler) {
        this.userRepository = userRepository;
        this.logoutHandler = logoutHandler;
        loadUsers();
    }

    @Override
    public void setLogManager(ILogManager logManager) {
        this.logManager = logManager;
    }

    @Override
    public void setAuthManager(IAuthManager authManager) {
        this.authManager = authManager;
    }

    private void loadUsers() {
        if (userRepository == null) return;
        try {
            userTable.setItems(FXCollections.observableArrayList(userRepository.findAll()));
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
            // 1. Hash the password
            String passwordHash  = PasswordUtil.hash(password);

            // 2. Create User Model
            IUser newUser = new User(username, passwordHash, role);

            // 3. Save to Repo
            userRepository.save(newUser);

            logManager.saveLog(LogType.SUCCESS, "Admin '%s' created new user '%s'".formatted(authManager.getCurrentUser().getUsername(),username));

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
        IUser selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            statusLabel.setText("Select a user to delete.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete User");
        alert.setContentText("Are you sure you want to delete " + selectedUser.getUsername() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            userRepository.delete(selectedUser);
            userTable.getItems().remove(selectedUser);
            statusLabel.setText("User deleted.");
        }
    }

    @FXML
    private void handleLogout() {
        if (logoutHandler != null) {
            logoutHandler.run();
        }
    }

    private void clearForm() {
        usernameField.clear();
        passwordField.clear();
        roleComboBox.getSelectionModel().clearSelection();
    }
}