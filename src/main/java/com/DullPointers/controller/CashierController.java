package com.DullPointers.controller;

import com.DullPointers.manager.AuthManager;
import com.DullPointers.manager.LogManager;
import com.DullPointers.manager.SaleManager;
import com.DullPointers.model.Customer;
import com.DullPointers.model.Sale;
import com.DullPointers.model.SaleLineItem;
import com.DullPointers.model.enums.PaymentMethod;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import com.DullPointers.repository.CustomerRepository;
import com.DullPointers.repository.SaleRepository;
import com.DullPointers.util.CustomerAnalytics;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.cell.TextFieldTableCell; // Import needed
import javafx.util.converter.IntegerStringConverter; // Import needed
import javafx.scene.layout.GridPane;

import java.math.BigDecimal;
import java.util.List;
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
    @FXML private TextField customerSearchField;
    @FXML private Label messageLabel;
    @FXML private Label subtotalLabel;
    @FXML private Label taxLabel;
    @FXML private Label grandTotalLabel;
    @FXML private Label customerNameLabel;
    @FXML private Label loyaltyPointsLabel;
    @FXML private Label discountLabel;

    @FXML private CheckBox redeemPointsCheckbox;
    @FXML private ComboBox<PaymentMethod> paymentMethodCombo;

    // --- Dependencies ---
    private LogManager logmanager;
    private SaleManager saleManager;
    private AuthManager authManager;
    private CustomerRepository customerRepository;
    private SaleRepository saleRepository;
    private Runnable logoutHandler;

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
                setGraphic(empty ? null : deleteBtn);
            }
        });

        // Ensure text field grabs focus on enter for barcode scanning
        barcodeField.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleAddItem();
            }
        });

        paymentMethodCombo.setItems(FXCollections.observableArrayList(PaymentMethod.values()));
        paymentMethodCombo.getSelectionModel().select(PaymentMethod.CASH);
    }

    public void setManagers(LogManager logManager, SaleManager saleManager, AuthManager authManager,
                            CustomerRepository customerRepo, SaleRepository saleRepo) {
        this.saleManager = saleManager;
        this.authManager = authManager;
        this.logmanager = logManager;
        this.customerRepository = customerRepo;
        this.saleRepository = saleRepo;

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
    private void handleCustomerSearch() {
        String query = customerSearchField.getText().trim();
        if(query.isEmpty()) return;
        List<Customer> results = customerRepository.search(query);

        if (results.isEmpty()) showAlert("Not Found", "No customer found.");
        else if (results.size() == 1) linkCustomer(results.get(0));
        else {
            ChoiceDialog<Customer> dialog = new ChoiceDialog<>(results.get(0), results);
            dialog.setTitle("Select Customer");
            dialog.setContentText("Choose one:");
            dialog.showAndWait().ifPresent(this::linkCustomer);
        }
    }

    @FXML
    private void handleNewCustomer() {
        Dialog<Customer> dialog = new Dialog<>();
        dialog.setTitle("New Customer");
        dialog.setHeaderText("Register");
        ButtonType createType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        TextField nameF = new TextField(); TextField phoneF = new TextField();
        grid.add(new Label("Name:"), 0, 0); grid.add(nameF, 1, 0);
        grid.add(new Label("Phone:"), 0, 1); grid.add(phoneF, 1, 1);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(btn -> {
            if (btn == createType) {
                Customer c = new Customer(phoneF.getText(), nameF.getText());
                customerRepository.save(c);
                return c;
            }
            return null;
        });
        dialog.showAndWait().ifPresent(this::linkCustomer);
    }

    @FXML
    private void handleRedeemToggle() {
        saleManager.toggleLoyaltyRedemption(redeemPointsCheckbox.isSelected());
        updateUI();
    }

    @FXML
    private void handleShowAnalytics() {
        if (saleManager.getCurrentSale().getCustomer() == null) {
            showAlert("Analytics", "No customer linked.");
            return;
        }
        String report = CustomerAnalytics.generateReport(saleManager.getCurrentSale().getCustomer(), saleRepository);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Analytics");
        alert.setHeaderText("Customer Report");
        alert.setContentText(report);
        alert.showAndWait();
    }

    @FXML
    private void handleCheckout() {
        Sale sale = saleManager.getCurrentSale();
        if (sale == null || sale.getItems().isEmpty()) {
            showAlert("Error", "Cart empty");
            return;
        }

        PaymentMethod method = paymentMethodCombo.getValue();
        BigDecimal amount = sale.getNetPayableAmount();

        if (method == PaymentMethod.LOYALTY_POINTS) {
            handleLoyaltyPayment(sale, amount);
        } else {
            TextInputDialog dialog = new TextInputDialog(amount.toString());
            dialog.setTitle("Payment");
            dialog.setHeaderText("Pay: " + amount);
            dialog.setContentText("Enter Amount:");
            dialog.showAndWait().ifPresent(val -> processPayment(new BigDecimal(val), method));
        }
    }

    private void handleLoyaltyPayment(Sale sale, BigDecimal amount) {
        if (sale.getCustomer() == null) {
            showAlert("Error", "Customer required for Loyalty Payment.");
            return;
        }
        int pointsNeeded = amount.intValue();
        if (sale.getCustomer().getLoyaltyPoints() < pointsNeeded) {
            showAlert("Failed", "Insufficient Points. Need: " + pointsNeeded);
            return;
        }
        processPayment(amount, PaymentMethod.LOYALTY_POINTS);
    }

    private void processPayment(BigDecimal amount, PaymentMethod method) {
        try {
            saleManager.addPayment(amount, method);
            saleManager.completeSale();
            showAlert("Success", "Sale Completed!");
            saleManager.startNewSale();
            updateUI();
        } catch (Exception e) {
            showAlert("Error", e.getMessage());
        }
    }

    @FXML private void handleLogout() { if(logoutHandler != null) logoutHandler.run(); }
    @FXML private void handleCancel() { saleManager.startNewSale(); updateUI(); }

    private void linkCustomer(Customer c) {
        saleManager.setCustomer(c);
        customerSearchField.clear();
        updateUI();
    }

    // --- Helper Methods ---

    private void updateUI() {
        Sale s = saleManager.getCurrentSale();
        if (s != null) {
            cartTable.setItems(FXCollections.observableArrayList(s.getItems()));
            cartTable.refresh();
            subtotalLabel.setText(s.calculateGrandTotal().toString());
            discountLabel.setText("-" + s.getPointsRedeemed());
            grandTotalLabel.setText(s.getNetPayableAmount().toString());

            if (s.getCustomer() != null) {
                customerNameLabel.setText(s.getCustomer().getName());
                loyaltyPointsLabel.setText("Pts: " + s.getCustomer().getLoyaltyPoints());
                redeemPointsCheckbox.setDisable(false);
            } else {
                customerNameLabel.setText("Guest");
                loyaltyPointsLabel.setText("-");
                redeemPointsCheckbox.setDisable(true);
                redeemPointsCheckbox.setSelected(false);
            }
        }
    }

    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title); a.setContentText(msg); a.showAndWait();
    }
}