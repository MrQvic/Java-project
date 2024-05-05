package org.openjfx.javaproject.room;
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

    public ControlledRobot(Position position, double angle) {
        this.position = position;
        this.angle = angle;
        this.shape = new Circle(RADIUS);
        this.directionLine = new Line();
        this.directionLine.setStartX(position.getX());
        this.directionLine.setStartY(position.getY());
        updateDirectionLine();
        updatePosition();
    }

    public static ControlledRobot create(Room room, Position position, double angle) {
        if (!room.canCreate(position, RADIUS)) {    //there is obstacle
            return null;
        }
        ControlledRobot robot = new ControlledRobot(position, angle);
        room.addControlledRobot(robot);
        return robot;
    }

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

    public Circle getShape() {
        return shape;
    }

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

    public Line getDirectionLine() {
        return directionLine;
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

    private boolean checkCollision(Autorobot robot, double nextX, double nextY) {
        double dx = nextX - robot.getPosition().getX();
        double dy = nextY - robot.getPosition().getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        return distance < (RADIUS + robot.getSize());
    }

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
