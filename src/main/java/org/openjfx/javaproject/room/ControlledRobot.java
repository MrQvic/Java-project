package org.openjfx.javaproject.room;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Circle;
import org.openjfx.javaproject.common.Obstacle;

public class ControlledRobot {
    private static final double TIME_STEP = 0.016; // 60 FPS
    private static final double SPEED = 50; // pixels per second
    private static final double RADIUS = 10; // radius of the robot

    private final Position position;
    private double angle;
    private final Circle shape;

    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    public ControlledRobot(Position position, double angle) {
        this.position = position;
        this.angle = angle;
        this.shape = new Circle(RADIUS);
        updatePosition();
    }

    public static ControlledRobot create(Room room, Position position, double angle) {
        ControlledRobot robot = new ControlledRobot(position, angle);
        room.addControlledRobot(robot);
        return robot;
    }

    public void update(Room room) {
        // Calculate velocity vector components based on user input
        double velX = (rightPressed ? 1 : 0) - (leftPressed ? 1 : 0);
        double velY = (downPressed ? 1 : 0) - (upPressed ? 1 : 0);

        // Normalize the velocity vector
        double length = Math.sqrt(velX * velX + velY * velY);
        if (length != 0) {
            velX /= length;
            velY /= length;
        }

        // Move the robot
        double nextX = position.getX() + velX * SPEED * TIME_STEP;
        double nextY = position.getY() + velY * SPEED * TIME_STEP;
        System.out.println(position.getX());
        // Check for collision with room edges
        if (nextX >= RADIUS && nextX <= room.getWidth() - RADIUS) {
            position.setX(nextX);
        }
        if (nextY >= RADIUS && nextY <= room.getHeight() - RADIUS) {
            position.setY(nextY);
        }

        updatePosition();
    }

    private void updatePosition() {
        // Update robot's position
        shape.setCenterX(position.getX());
        shape.setCenterY(position.getY());
    }

    public Circle getShape() {
        return shape;
    }

    public void keyPressed(KeyEvent event) {
        KeyCode code = event.getCode();
        if (code == KeyCode.UP) {
            upPressed = true;
        } else if (code == KeyCode.DOWN) {
            downPressed = true;
        } else if (code == KeyCode.LEFT) {
            leftPressed = true;
        } else if (code == KeyCode.RIGHT) {
            rightPressed = true;
        }
    }

    public void keyReleased(KeyEvent event) {
        KeyCode code = event.getCode();
        if (code == KeyCode.UP) {
            upPressed = false;
        } else if (code == KeyCode.DOWN) {
            downPressed = false;
        } else if (code == KeyCode.LEFT) {
            leftPressed = false;
        } else if (code == KeyCode.RIGHT) {
            rightPressed = false;
        }
    }

    public Position getPosition() {
        return position;
    }

    public String getPositionAsString() {
        return String.format("x: %.2f, y: %.2f", position.getX(), position.getY());
    }

    public double getSize() {
        return RADIUS;
    }

    public double getAngle() {
        return angle;
    }
}
