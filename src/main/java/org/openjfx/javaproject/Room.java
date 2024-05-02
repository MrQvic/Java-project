package org.openjfx.javaproject;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private double width;
    private double height;
    private List<Circle> obstacles; // List of obstacles

    public Room(double width, double height) {
        this.width = width;
        this.height = height;
        this.obstacles = new ArrayList<>(); // Initialize the list
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

    public void addObstacle(Circle obstacle) {
        obstacles.add(obstacle);
    }

    public List<Circle> getObstacles() {
        return obstacles;
    }
}
