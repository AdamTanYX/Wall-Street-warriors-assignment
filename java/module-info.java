module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires java.sql;

    requires java.mail;
    requires kernel;
    requires layout;
    requires javafx.web;


    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
    exports WallStreetGUI;
    opens WallStreetGUI to javafx.fxml;
    opens WallStreet to javafx.base;
}