package com.DullPointers.controller;

import com.DullPointers.manager.AuthManager;
import com.DullPointers.manager.SaleManager;
import com.DullPointers.model.Sale;
import com.DullPointers.model.SaleLineItem;
import com.DullPointers.model.enums.PaymentMethod;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.math.BigDecimal;
import java.util.Optional;

public class CashierController {


    // --- UI Components ---
    @FXML private Label currentUserLabel;
    @FXML private TableView<SaleLineItem> cartTable;
    @FXML private TableColumn<SaleLineItem, Void> colDelete;
    @FXML private TableColumn<SaleLineItem, String> colProduct;
    @FXML private TableColumn<SaleLineItem, BigDecimal> colPrice;
    @FXML private TableColumn<SaleLineItem, Integer> colQty;
    @FXML private TableColumn<SaleLineItem, BigDecimal> colTotal;

    @FXML private TextField barcodeField;
    @FXML private Label messageLabel;
    @FXML private Label subtotalLabel;
    @FXML private Label taxLabel;
    @FXML private Label grandTotalLabel;

    // --- Dependencies ---
    private SaleManager saleManager;
    private AuthManager authManager;
    private Runnable logoutHandler; // Callback for logout navigation

    // --- Initialization ---
    @FXML
    public void initialize() {
        // 1. Enable Editing on the Table
        cartTable.setEditable(true);

        // --- Column Setup ---
        colProduct.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProduct().getName()));
        colPrice.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getSnapshotPrice()));
        colTotal.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getSubTotal()));

        // --- QUANTITY COLUMN (EDITABLE) ---
        // A. Bind the data
        colQty.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getQuantity()));

        // B. Make the cell an editable Text Field that accepts Integers
        colQty.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        // C. Handle the "Enter Key" event (Commit)
        colQty.setOnEditCommit(event -> {
            SaleLineItem item = event.getRowValue();
            Integer newQty = event.getNewValue();

            // Validation: Quantity must be positive
            if (newQty != null && newQty > 0) {
                // Update Model
                item.setQuantity(newQty);

                // Recalculate Totals & Refresh UI
                // We call updateUI() to ensure the 'Total' column and 'Grand Total' labels update immediately
                updateUI();
                messageLabel.setText("Updated qty for " + item.getProduct().getName());
                messageLabel.setStyle("-fx-text-fill: green;");
            } else {
                // Invalid input: Refresh to revert the visual change in the table
                messageLabel.setText("Quantity must be greater than 0");
                messageLabel.setStyle("-fx-text-fill: red;");
                cartTable.refresh();
            }
        });

        // --- DELETE BUTTON COLUMN (From previous step) ---
        colDelete.setCellFactory(param -> new TableCell<>() {
            private final Button deleteBtn = new Button("X");
            {
                deleteBtn.getStyleClass().add("table-delete-btn"); // Ensure this class exists in CSS
                deleteBtn.setOnAction(event -> {
                    SaleLineItem item = getTableView().getItems().get(getIndex());
                    handleDeleteItem(item);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else setGraphic(deleteBtn);
            }
        });

        // Ensure text field grabs focus on enter for barcode scanning
        barcodeField.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleAddItem();
            }
        });
    }

    public void setManagers(SaleManager saleManager, AuthManager authManager) {
        this.saleManager = saleManager;
        this.authManager = authManager;

        // Start a new sale session immediately
        saleManager.startNewSale();

        updateUI();
    }

    public void setLogoutHandler(Runnable handler) {
        this.logoutHandler = handler;
    }

    // --- Event Handlers ---

    @FXML
    private void handleAddItem() {
        String barcode = barcodeField.getText().trim();
        if (barcode.isEmpty()) return;

        try {
            // Defaulting quantity to 1 for simple scan
            saleManager.addItemToSale(barcode, 1);
            messageLabel.setText("Added: " + barcode);
            messageLabel.setStyle("-fx-text-fill: green;");
            barcodeField.clear();
            updateUI(); // Refresh table and totals
        } catch (Exception e) {
            messageLabel.setText("Error: " + e.getMessage());
            messageLabel.setStyle("-fx-text-fill: red;");
        }
        barcodeField.requestFocus();
    }

    @FXML
    private void handleDeleteItem(SaleLineItem item) {
        // 1. Call Manager to remove logic
        saleManager.removeItemFromSale(item);

        // 2. Refresh UI
        updateUI();

        // Optional: Reset focus to barcode field for speed
        barcodeField.requestFocus();
    }

    @FXML
    private void handleCheckout() {
        Sale sale = saleManager.getCurrentSale();
        if (sale == null || sale.getItems().isEmpty()) {
            messageLabel.setText("Cart is empty!");
            return;
        }

        // Simple payment flow (Assume Cash for now, or ask user)
        TextInputDialog dialog = new TextInputDialog(sale.calculateGrandTotal().toString());
        dialog.setTitle("Payment");
        dialog.setHeaderText("Total Due: " + sale.calculateGrandTotal());
        dialog.setContentText("Enter Cash Amount:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                BigDecimal cashGiven = new BigDecimal(result.get());

                // 1. Add Payment
                saleManager.addPayment(cashGiven, PaymentMethod.CASH);

                // 2. Complete Sale
                saleManager.completeSale();

                // 3. Feedback
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sale Complete");
                alert.setHeaderText("Transaction Successful");
                alert.setContentText("Receipt generated.");
                alert.showAndWait();

                // 4. Reset for next customer
                saleManager.startNewSale();
                updateUI();

            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Payment Failed: " + e.getMessage());
                alert.show();
            }
        }
    }

    @FXML
    private void handleCancel() {
        saleManager.startNewSale(); // Simply discard and start over
        messageLabel.setText("Sale Voided.");
        updateUI();
    }

    @FXML
    private void handleLogout() {
        if (logoutHandler != null) logoutHandler.run();
    }

    // --- Helper Methods ---

    private void updateUI() {
        // 1. Refresh Table
        Sale currentSale = saleManager.getCurrentSale();
        if (currentSale != null) {
            ObservableList<SaleLineItem> items = FXCollections.observableArrayList(currentSale.getItems());
            cartTable.setItems(items);
            cartTable.refresh();

            // 2. Refresh Totals
            BigDecimal total = currentSale.calculateGrandTotal();
            subtotalLabel.setText(String.format("%.2f", total));
            grandTotalLabel.setText(String.format("$ %.2f", total));
        } else {
            cartTable.getItems().clear();
            subtotalLabel.setText("0.00");
            grandTotalLabel.setText("$ 0.00");
        }

        // 3. Refresh User Info
        if (authManager != null) {
            try {
                currentUserLabel.setText("User: " + authManager.getCurrentUser().getUsername());
            } catch (Exception e) {
                currentUserLabel.setText("User: Unknown");
            }
        }
    }
}