package com.DullPointers.controller;

import com.DullPointers.manager.INotificationManager;
import com.DullPointers.manager.ILogManager;
import com.DullPointers.model.*;
import com.DullPointers.repository.ProductRepository;
import com.DullPointers.repository.SaleRepository;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;


import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

public class ManagerController implements IManagerController {

    // Tab 1
    @FXML private TableView<IProduct> productTable;
    @FXML private TableColumn<IProduct, String> colBarcode;
    @FXML private TableColumn<IProduct, String> colName;
    @FXML private TableColumn<IProduct, Integer> colStock;
    @FXML private TableColumn<IProduct, String> colPrice;

    @FXML private TextField barcodeField;
    @FXML private TextField nameField;
    @FXML private TextField priceField;
    @FXML private TextField stockField;
    @FXML private Label msgLabel;
    @FXML private ListView<String> notificationList;

    // --- Tab 2: Sales ---
    @FXML private TableView<ISale> salesTable;
    @FXML private TableColumn<ISale, String> colSaleId;
    @FXML private TableColumn<ISale, String> colSaleDate;
    @FXML private TableColumn<ISale, BigDecimal> colSaleTotal;

    // --- Tab 3: Reports ---
    @FXML private ComboBox<String> reportTypeCombo;
    @FXML private VBox productSelectContainer;
    @FXML private ComboBox<IProduct> productReportCombo; // For Product Performance Report
    @FXML private TextArea reportOutputArea;

    private ILogManager logManager;
    private ProductRepository productRepository;
    private SaleRepository saleRepository;
    private INotificationManager notificationManager;
    private Runnable logoutHandler;

    @FXML
    @Override
    public void initialize() {
        // 1. Existing Column Setup
        colBarcode.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getBarcode()));
        colName.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));
        colStock.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getStockQuantity()));
        colPrice.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getSellingPrice().toString()));

        // 2. NEW: Add Selection Listener
        productTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateForm(newSelection);
            }
        });
    }

    private void initSalesTab() {
        // Assuming ISale has getId(), getSaleDate(), getTotal()
        colSaleId.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getId())));
        colSaleDate.setCellValueFactory(cell -> {
            if(cell.getValue().getSaleDate() != null)
                return new SimpleStringProperty(cell.getValue().getSaleDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            return new SimpleStringProperty("");
        });
        colSaleTotal.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getNetPayableAmount()));
    }

    private void initReportsTab() {
        reportTypeCombo.setItems(FXCollections.observableArrayList(
                "Product Performance Report",
                "Cashier Performance Report",
                "Monthly Sales Report"
        ));

        // Show/Hide Product Selector based on Report Type
        reportTypeCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            boolean isProductReport = "Product Performance Report".equals(newVal);
            productSelectContainer.setVisible(isProductReport);
            productSelectContainer.setManaged(isProductReport);
        });

        // Setup Product Combo formatting
        productReportCombo.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(IProduct item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getName() + " (" + item.getBarcode() + ")");
            }
        });
        productReportCombo.setButtonCell(productReportCombo.getCellFactory().call(null));
    }

    private void populateForm(IProduct product) {
        barcodeField.setText(product.getBarcode());
        nameField.setText(product.getName());

        // Handle BigDecimal conversion for price
        if (product.getSellingPrice() != null) {
            priceField.setText(product.getSellingPrice().toString());
        } else {
            priceField.clear();
        }

        stockField.setText(String.valueOf(product.getStockQuantity()));

        // Optional: Give visual feedback that we are in "Edit Mode"
        msgLabel.setText("Editing: " + product.getName());
        msgLabel.setStyle("-fx-text-fill: blue;");
    }

    @FXML
    private void handleGenerateReport() {
        String type = reportTypeCombo.getValue();
        if (type == null) {
            reportOutputArea.setText("Please select a report type.");
            return;
        }

        try {
            Report report = null;

            switch (type) {
                case "Product Performance Report":
                    IProduct selectedProd = productReportCombo.getValue();
                    if (selectedProd == null) {
                        reportOutputArea.setText("Please select a product to analyze.");
                        return;
                    }
                    // Using the class provided in your prompt
                    report = new ProductPerformanceReport(saleRepository, selectedProd);
                    break;

                case "Cashier Performance Report":
                    // Placeholder: Instantiate your CashierReport class here
                    // report = new CashierReport(saleRepository);
                    reportOutputArea.setText("Cashier Report logic not yet implemented.");
                    return;

                case "Monthly Sales Report":
                    // Placeholder: Instantiate your TimePeriodReport here
                    // report = new MonthlyReport(saleRepository);
                    reportOutputArea.setText("Monthly Report logic not yet implemented.");
                    return;
            }

            if (report != null) {
                // Assuming Report class has a public method exposed, usually generate() or toString()
                // But ProductPerformanceReport overrides protected createReport().
                // Assuming you add a public getter in Report class: public String generate() { return createReport(); }
                String status = report.generateReport();
                reportOutputArea.setText(status);
            }

        } catch (Exception e) {
            reportOutputArea.setText("Error generating report: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRefreshSales() {
        if(saleRepository != null) {
            salesTable.setItems(FXCollections.observableArrayList(saleRepository.findAll()));
        }
    }

    @Override
    public void setDependencies(SaleRepository saleRepository, ILogManager logman, ProductRepository repo, INotificationManager notifMgr, Runnable logout) {
        this.logManager = logman;
        this.productRepository = repo;
        this.notificationManager = notifMgr;
        this.logoutHandler = logout;
        this.saleRepository = saleRepository;
        loadProducts();
        loadNotifications();
    }

    @FXML
    private void handleSaveProduct() {
        try {
            String barcode = barcodeField.getText();
            String name = nameField.getText();
            BigDecimal price = new BigDecimal(priceField.getText());
            int stock = Integer.parseInt(stockField.getText());

            // Create new product object
            IProduct product = new Product(barcode, name, price, stock);

            // Save to repo (Assumes your ProductRepository has a save method)
            productRepository.save(product);

            msgLabel.setText("Saved: " + name);
            msgLabel.setStyle("-fx-text-fill: green;");

            loadProducts(); // Refresh table
            handleClear();  // Reset form

        } catch (NumberFormatException e) {
            msgLabel.setText("Invalid Price or Stock format.");
            msgLabel.setStyle("-fx-text-fill: red;");
        } catch (Exception e) {
            msgLabel.setText("Error: " + e.getMessage());
            msgLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void handleClear() {
        barcodeField.clear();
        nameField.clear();
        priceField.clear();
        stockField.clear();
        msgLabel.setText(""); // Clear status message

        // Deselect the row so the user knows they are creating a NEW entry
        productTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleRefresh() {
        loadProducts();
    }

    @Override
    public void setLogManager(ILogManager logManager) {
        this.logManager = logManager;
    }

    @FXML
    private void handleLogout() {
        if (logoutHandler != null) logoutHandler.run();
    }

    private void loadProducts() {
        if(productRepository != null) productTable.setItems(FXCollections.observableArrayList(productRepository.findAll()));
    }

    private void loadNotifications() {
        if(notificationManager != null) notificationList.getItems().setAll(notificationManager.getAlerts());
    }

    @FXML private void handleClearNotifications() {
        if(notificationManager != null) notificationManager.clearAlerts();
        loadNotifications();
    }
}