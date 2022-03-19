module com.simeon.pizzaria_bella_napoli {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;

    opens com.simeon.pizzaria_bella_napoli to javafx.fxml;
    exports com.simeon.pizzaria_bella_napoli;
}