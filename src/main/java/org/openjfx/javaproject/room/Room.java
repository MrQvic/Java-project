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
    public ControlledRobot controlledRobot;

    /**
     * Constructs a Room object with the specified width and height.
     *
     * @param width  The width of the room.
     * @param height The height of the room.
     */
    public Room(double width, double height) {
        this.width = width;
        this.height = height;
        this.obstacles = new ArrayList<>();
        this.robots = new ArrayList<>();
    }

    /**
     * Creates a JavaFX Pane representing the room with its border.
     *
     * @return The JavaFX Pane representing the room.
     */
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

    /**
     * Retrieves the width of the room.
     *
     * @return The width of the room.
     */
    public double getWidth() {
        return width;
    }

    /**
     * Retrieves the height of the room.
     *
     * @return The height of the room.
     */
    public double getHeight() {
        return height;
    }

    /**
     * Adds an obstacle to the room.
     *
     * @param obstacle The obstacle to add.
     */
    public void addObstacle(Obstacle obstacle) {
        obstacles.add(obstacle);
    }

    /**
     * Retrieves the list of obstacles in the room.
     *
     * @return The list of obstacles.
     */
    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    /**
     * Adds an autorobot to the room.
     *
     * @param robot The autorobot to add.
     */
    public void addRobot(Autorobot robot) {
        if (!this.robots.contains(robot)) {
            this.robots.add(robot);
        }
    }

    /**
     * Retrieves the list of autorobots in the room.
     *
     * @return The list of autorobots.
     */
    public List<Autorobot> getRobots() {
        return /*Collections.unmodifiableList(this.*/robots/*)*/;
    }

    /**
     * Clears all autorobots from the room.
     */
    public void clear() {
        this.robots.clear();
    }

    /**
     * Checks if a controlled robot is set in the room.
     *
     * @return True if a controlled robot is set, false otherwise.
     */
    public boolean isControlledRobotSet(){
        return this.controlledRobot != null;
    }

    /**
     * Adds a controlled robot to the room.
     *
     * @param controlledRobot The controlled robot to add.
     * @return True if the controlled robot is added successfully, false otherwise.
     */
    public boolean addControlledRobot(ControlledRobot controlledRobot) {
        if (this.controlledRobot == null) {
            this.controlledRobot = controlledRobot;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if a robot can be created at the specified position without colliding with other robots or obstacles.
     *
     * @param position The position where the robot is to be created.
     * @param radius   The radius of the robot.
     * @return True if a robot can be created at the specified position, false otherwise.
     */
    public boolean canCreate(Position position, double radius){
        // loop through robots
        for (Autorobot robot : robots) {
            if (position.isNear(robot.getPosition(), radius + robot.getSize())) {
                return false;
            }
        }
        // loop through obstacles
        for (Obstacle obstacle : obstacles) {
            if (obstacle instanceof RectangleObstacle squareObstacle) {
                double left = squareObstacle.getPosition().getX() - squareObstacle.getSize() / 2 - radius ;
                double right = squareObstacle.getPosition().getX() + squareObstacle.getSize() / 2 + radius ;
                double top = squareObstacle.getPosition().getY() - squareObstacle.getSize() / 2 - radius ;
                double bottom = squareObstacle.getPosition().getY() + squareObstacle.getSize() / 2 + radius ;
                if (position.getX() > left && position.getX() < right && position.getY() > top && position.getY() < bottom) {
                    return false;
                }
            } else {
                if (position.isNear(obstacle.getPosition(), radius + obstacle.getSize())) {
                    return false;
                }
            }
        }
        double x = position.getX();
        double y = position.getY();

        return !(x - radius < 0) && !(x + radius > width) && !(y - radius < 0) && !(y + radius > height);
    }

    /**
     * Retrieves the controlled robot in the room.
     *
     * @return The controlled robot in the room.
     */
    public ControlledRobot getControlledRobot(){
        return this.controlledRobot;
    }

    /**
     * Clears all autorobots and obstacles from the room.
     */
    public void clearAll() {
        this.robots.clear();
        this.obstacles.clear();
        this.controlledRobot = null;
    }
}
