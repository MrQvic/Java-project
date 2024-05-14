module org.openjfx.javaproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;

    opens org.openjfx.javaproject to javafx.fxml;
    exports org.openjfx.javaproject;
}