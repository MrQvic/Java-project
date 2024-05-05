package org.openjfx.javaproject.room;
import javafx.scene.paint.Color;
import org.openjfx.javaproject.common.Obstacle;
import org.openjfx.javaproject.room.CircleObstacle;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class ControlledRobot {
    private static final double TIME_STEP = 0.016; // 60 FPS
    private static final double SPEED = 100; // pixels per second
    private static final double RADIUS = 10; // radius of the robot

    private final Position position;
    private double angle;
    private final Circle shape;
    private final Line directionLine;

    private boolean wPressed = false;
    private boolean aPressed = false;
    private boolean sPressed = false;
    private boolean dPressed = false;

    /**
     * Constructs a ControlledRobot with the specified position and angle.
     *
     * @param position The initial position of the robot.
     * @param angle The initial angle of the robot.
     */
    public ControlledRobot(Position position, double angle) {
        this.position = position;
        this.angle = angle;
        this.shape = new Circle(RADIUS);
        shape.setFill(Color.PURPLE);
        this.directionLine = new Line();
        this.directionLine.setStartX(position.getX());
        this.directionLine.setStartY(position.getY());
        updateDirectionLine();
        updatePosition();
    }

    /**
     * Creates a ControlledRobot in the specified room with the given position and angle.
     *
     * @param room The room in which the robot is created.
     * @param position The initial position of the robot.
     * @param angle The initial angle (in degrees) of the robot.
     * @return The created ControlledRobot instance, or null if it cannot be created due to obstacle collision.
     */
    public static ControlledRobot create(Room room, Position position, double angle) {
        if (!room.canCreate(position, RADIUS)) {    //there is obstacle
            return null;
        }
        ControlledRobot robot = new ControlledRobot(position, angle);
        room.addControlledRobot(robot);
        return robot;
    }

    /**
     * Updates the state of the robot based on user input and collision detection.
     *
     * @param room The room in which the robot exists.
     */
    public void update(Room room) {
        // Rotate left and right
        if (aPressed) {
            angle -= 5;
        }
        if (dPressed) {
            angle += 5;
        }

        if (wPressed) {
            double velX = Math.cos(Math.toRadians(angle)) * SPEED * TIME_STEP;
            double velY = Math.sin(Math.toRadians(angle)) * SPEED * TIME_STEP;

            // Calculate new position
            double nextX = position.getX() + velX;
            double nextY = position.getY() + velY;

            // Check collision with robots
            for (Autorobot robot : room.getRobots()) {
                if (checkCollision(robot, nextX, nextY)) {
                    updateDirectionLine();
                    return;
                }
            }

            // Check collision with obstacles
            for (Obstacle obstacle : room.getObstacles()) {
                if (checkCollision(obstacle, nextX, nextY)) {
                    updateDirectionLine();
                    return;
                }
            }

            // Collision check with room boundaries
            if (nextX >= RADIUS && nextX <= room.getWidth() - RADIUS) {
                position.setX(nextX);
            }
            if (nextY >= RADIUS && nextY <= room.getHeight() - RADIUS) {
                position.setY(nextY);
            }
        }
        updateDirectionLine();
        updatePosition();
    }


    private void updatePosition() {
        // Update robot's position
        shape.setCenterX(position.getX());
        shape.setCenterY(position.getY());
    }

    /**
     * Retrieves the graphical representation of the robot.
     *
     * @return The Circle representing the robot's shape.
     */
    public Circle getShape() {
        return shape;
    }

    /**
     * Handles key press events.
     *
     * @param event The KeyEvent representing the key press event.
     */
    public void keyPressed(KeyEvent event) {
        KeyCode code = event.getCode();
        if (code == KeyCode.W) {
            wPressed = true;
        } else if (code == KeyCode.A) {
            aPressed = true;
        } else if (code == KeyCode.S) {
            sPressed = true;
        } else if (code == KeyCode.D) {
            dPressed = true;
        }
    }

    /**
     * Handles key release events.
     *
     * @param event The KeyEvent representing the key release event.
     */
    public void keyReleased(KeyEvent event) {
        KeyCode code = event.getCode();
        if (code == KeyCode.W) {
            wPressed = false;
        } else if (code == KeyCode.A) {
            aPressed = false;
        } else if (code == KeyCode.S) {
            sPressed = false;
        } else if (code == KeyCode.D) {
            dPressed = false;
        }
    }

    private void updateDirectionLine() {
        double startX = position.getX(); // Start X is the robot's current X position
        double startY = position.getY(); // Start Y is the robot's current Y position
        double endX = position.getX() + Math.cos(Math.toRadians(angle)) * RADIUS * 1.5;
        double endY = position.getY() + Math.sin(Math.toRadians(angle)) * RADIUS * 1.5;

        directionLine.setStartX(startX);
        directionLine.setStartY(startY);
        directionLine.setEndX(endX);
        directionLine.setEndY(endY);
    }

    /**
     * Retrieves the line representing the direction the robot is facing.
     *
     * @return The Line representing the direction.
     */
    public Line getDirectionLine() {
        return directionLine;
    }

    /**
     * Retrieves the position of the robot.
     *
     * @return The Position object representing the robot's position.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Retrieves a string representation of the robot's position and angle.
     *
     * @return A string representing the robot's position and angle in the format "x y angle".
     */
    public String getPositionAsString() {
        return String.format("%.2f %.2f %.2f", position.getX(), position.getY(), getAngle());
    }

    /**
     * Retrieves the size of the robot.
     *
     * @return The size of the robot, which is its radius.
     */
    public double getSize() {
        return RADIUS;
    }

    /**
     * Retrieves the angle the robot is facing.
     *
     * @return The angle the robot is facing, in degrees.
     */
    public double getAngle() {
        return angle;
    }

    /**
     * Checks if the ControlledRobot collides with an Autorobot at the specified next position.
     *
     * @param robot The Autorobot to check collision against.
     * @param nextX The x-coordinate of the next position of the ControlledRobot.
     * @param nextY The y-coordinate of the next position of the ControlledRobot.
     * @return True if the ControlledRobot collides with the specified Autorobot, false otherwise.
     */
    private boolean checkCollision(Autorobot robot, double nextX, double nextY) {
        double dx = nextX - robot.getPosition().getX();
        double dy = nextY - robot.getPosition().getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        return distance < (RADIUS + robot.getSize());
    }

    /**
     * Checks if the ControlledRobot collides with an Obstacle at the specified next position.
     *
     * @param obstacle The Obstacle to check collision against.
     * @param nextX The x-coordinate of the next position of the ControlledRobot.
     * @param nextY The y-coordinate of the next position of the ControlledRobot.
     * @return True if the ControlledRobot collides with the specified Obstacle, false otherwise.
     */
    private boolean checkCollision(Obstacle obstacle, double nextX, double nextY) {
        if (obstacle instanceof CircleObstacle circleObstacle) {
            double dx = nextX - circleObstacle.getPosition().getX();
            double dy = nextY - circleObstacle.getPosition().getY();
            double distance = Math.sqrt(dx * dx + dy * dy);
            return distance < (RADIUS + circleObstacle.getSize());
        } else if (obstacle instanceof RectangleObstacle squareObstacle) {
            double halfSize = squareObstacle.getSize() / 2;
            double left = squareObstacle.getPosition().getX() - halfSize - RADIUS;
            double right = squareObstacle.getPosition().getX() + halfSize + RADIUS;
            double top = squareObstacle.getPosition().getY() - halfSize - RADIUS;
            double bottom = squareObstacle.getPosition().getY() + halfSize + RADIUS;
            return nextX >= left && nextX <= right && nextY >= top && nextY <= bottom;
        }
        return false;
    }


}
