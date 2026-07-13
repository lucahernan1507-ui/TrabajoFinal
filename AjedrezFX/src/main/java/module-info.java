module AjedrezFX {

    requires javafx.controls;
    requires javafx.fxml;

    exports application;
    exports controller;
    exports model;
    exports piezas;

    opens application to javafx.fxml;
    opens controller to javafx.fxml;

}