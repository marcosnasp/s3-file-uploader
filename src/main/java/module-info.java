module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires software.amazon.awssdk.core;
    requires software.amazon.awssdk.regions;
    requires software.amazon.awssdk.services.s3;

    opens com.example to javafx.controls, javafx.fxml, software.amazon.awssdk.core, software.amazon.awssdk.regions,
            software.amazon.awssdk.services.s3;

    exports com.example;
}
