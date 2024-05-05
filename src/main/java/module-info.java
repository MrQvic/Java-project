module org.openjfx.javaproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;


    opens org.openjfx.javaproject to javafx.fxml;
    exports org.openjfx.javaproject;
    exports org.openjfx.javaproject.room;
    opens org.openjfx.javaproject.room to javafx.fxml;
    exports org.openjfx.javaproject.common;
    opens org.openjfx.javaproject.common to javafx.fxml;
}