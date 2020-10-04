module com.marufeb {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.marufeb to javafx.fxml;
    exports com.marufeb.model;
    exports com.marufeb;
}