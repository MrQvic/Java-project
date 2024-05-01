module org.openjfx.javaproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.openjfx.javaproject to javafx.fxml;
    exports org.openjfx.javaproject;
}