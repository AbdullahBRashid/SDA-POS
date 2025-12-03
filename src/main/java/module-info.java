module com.DullPointers {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;

    requires jbcrypt;

    opens com.DullPointers.controller to javafx.fxml;
    opens com.DullPointers.model to com.fasterxml.jackson.databind;
    opens com.DullPointers.model.enums to com.fasterxml.jackson.databind;

    exports com.DullPointers;
    opens com.DullPointers.manager to javafx.fxml;
}