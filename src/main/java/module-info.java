module com.example.fileseparator {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.fileseparator to javafx.fxml;
    exports com.example.fileseparator;
}