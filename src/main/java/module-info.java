module com.pagereplacementalgorithm {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.pagereplacementalgorithm to javafx.fxml;
    exports com.pagereplacementalgorithm;
    exports com.pagereplacementalgorithm.controller;
    opens com.pagereplacementalgorithm.controller to javafx.fxml;
    exports com.pagereplacementalgorithm.splash;
    opens com.pagereplacementalgorithm.splash to javafx.fxml;
}