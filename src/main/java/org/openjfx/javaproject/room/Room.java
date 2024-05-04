package org.openjfx.javaproject.room;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.openjfx.javaproject.common.Obstacle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Room {
    private final double width;
    private final double height;
    private final List<Obstacle> obstacles; // List of obstacles

    private final List<Autorobot> robots;

    public Room(double width, double height) {
        this.width = width;
        this.height = height;
        this.obstacles = new ArrayList<>();
        this.robots = new ArrayList<>();
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

    public void addObstacle(Obstacle obstacle) {
        obstacles.add(obstacle);
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public void addRobot(Autorobot robot) {
        this.robots.add(robot);
    }

    public List<Autorobot> getRobots() {
        return Collections.unmodifiableList(this.robots);
    }

    public void clear() {
        this.robots.clear();
    }

    public boolean canCreate(Position position, double radius){
        // loop trough robots
        for (Autorobot robot : robots) {
            if (position.isNear(robot.getPosition(), radius + robot.getSize())) {
                return false;
            }
        }

        // loop trough obstacles
        for (Obstacle obstacle : obstacles) {
            if (position.isNear(obstacle.getPosition(), radius + obstacle.getSize())) {
                return false;
            }
        }

        return true;
    }
}
