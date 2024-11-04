module org.example.ejei {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.ejei to javafx.fxml;
    exports org.example.ejei;
}