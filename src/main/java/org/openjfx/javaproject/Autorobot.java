package org.openjfx.javaproject;

import javafx.scene.shape.Circle;

public class Autorobot {
    private static final double TIME_STEP = 0.016; // 60 FPS
    private static final double SPEED = 50; // pixels per second
    private static final double RADIUS = 10; // radius of the robot

    private static final double SAFE_ZONE = 20; // distance from the edge

    private double x;
    private double y;
    private double angle;
    private Circle shape;

    public Autorobot(double x, double y) {
        this.x = x;
        this.y = y;
        this.angle = 0;
        this.shape = new Circle(10);
        updatePosition();
    }


    public void update(Room room) {
        // Calculate velocity vector components
        double velX = SPEED * Math.cos(angle);
        double velY = SPEED * Math.sin(angle);

        // Predict next position
        double nextX = x + velX * TIME_STEP;
        double nextY = y + velY * TIME_STEP;

        // Check for collision with room edges
        if (isNearEdge(nextX, nextY, room)) {
            angle += 0.05;
        }
        // Update position
        x = nextX;
        y = nextY;


        updatePosition();
    }

    private boolean isNearEdge(double nextX, double nextY, Room room) {
        return nextX - RADIUS - SAFE_ZONE < 0 || nextX + RADIUS + SAFE_ZONE > room.getWidth() || nextY - RADIUS - SAFE_ZONE < 0 || nextY + RADIUS + SAFE_ZONE > room.getHeight();
    }

    private void updatePosition() {
        // Update robot's position
        shape.setCenterX(x);
        shape.setCenterY(y);
    }

    public Circle getShape() {
        return shape;
    }
}


