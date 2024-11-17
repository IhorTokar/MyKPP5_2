module com.example.mykpp5_2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires com.dlsc.formsfx;
    requires java.sql;

    opens com.example.mykpp5_2 to javafx.fxml;
    exports com.example.mykpp5_2;
}