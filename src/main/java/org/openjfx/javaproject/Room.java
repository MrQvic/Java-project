package org.openjfx.javaproject;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Room {
    private double width;
    private double height;

    public Room(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public Pane create() {
        Pane room = new Pane();
        room.setPrefSize(width, height);

        // Create a border
        Rectangle border = new Rectangle(width, height);
        border.setFill(Color.TRANSPARENT);
        border.setStroke(Color.BLACK);
        border.setStrokeWidth(3.0);

        room.getChildren().add(border);
        return room;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
