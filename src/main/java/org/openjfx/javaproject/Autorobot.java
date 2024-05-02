package org.openjfx.javaproject;

import javafx.scene.shape.Circle;

public class Autorobot {
    private static final double TIME_STEP = 0.016; // 60 FPS
    private static final double SPEED = 50; // pixels per second
    private static final double RADIUS = 10; // radius of the robot

    private static final double SAFE_ZONE = 10; // distance from the edge

    private static final double VIEW_DISTANCE = 75; // how far the robot can see
    private static final double VIEW_WIDTH = 20; // how wide the view is

    private static final double VIEW_ANGLE = Math.PI / 6; // 30 degrees

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

        for (Circle obstacle : room.getObstacles()) {
            double distanceToObstacle = Math.hypot(x - obstacle.getCenterX(), y - obstacle.getCenterY());
            if (distanceToObstacle < RADIUS + obstacle.getRadius() + SAFE_ZONE) {
                // Avoid the obstacle
                angle += 0.1;
            }
        }
        if(isInViewOfEdgeCenter(nextX, nextY, room)){
            angle += 0.1;
        } else if (isInViewOfEdgeLeft(nextX, nextY, room)){
            angle += 0.1;
        } else if (isInViewOfEdgeRight(nextX, nextY, room)) {
            angle -= 0.1;
        }

        // Check for collision with room edges
        if (isNearEdge(nextX, nextY, room) /*|| isInViewOfEdge(nextX, nextY, room)*/) {
            angle += 0.1;
        }
        // Update position
        x = nextX;
        y = nextY;


        updatePosition();
    }

    private boolean isNearEdge(double nextX, double nextY, Room room) {
        double leftEdge = RADIUS + SAFE_ZONE;
        double rightEdge = room.getWidth() - RADIUS - SAFE_ZONE;
        double topEdge = RADIUS + SAFE_ZONE;
        double bottomEdge = room.getHeight() - RADIUS - SAFE_ZONE;

        return nextX < leftEdge || nextX > rightEdge || nextY < topEdge || nextY > bottomEdge;
    }


    private boolean isInViewOfEdge(double nextX, double nextY, Room room) {
        // Calculate the corners of the view rectangle
        double leftX = nextX + VIEW_DISTANCE * Math.cos(angle) - VIEW_WIDTH * Math.sin(angle) / 2;
        double leftY = nextY + VIEW_DISTANCE * Math.sin(angle) + VIEW_WIDTH * Math.cos(angle) / 2;
        double rightX = nextX + VIEW_DISTANCE * Math.cos(angle) + VIEW_WIDTH * Math.sin(angle) / 2;
        double rightY = nextY + VIEW_DISTANCE * Math.sin(angle) - VIEW_WIDTH * Math.cos(angle) / 2;

        // Check if any of the corners are outside the room
        return leftX < 0 || rightX > room.getWidth() || leftY < 0 || rightY > room.getHeight();
    }

    private void updatePosition() {
        // Update robot's position
        shape.setCenterX(x);
        shape.setCenterY(y);
    }

    public Circle getShape() {
        return shape;
    }

    private boolean isInViewOfEdgeLeft(double nextX, double nextY, Room room) {
        // Calculate the endpoints of the visibility lines
        double leftEndX = nextX + VIEW_DISTANCE * Math.cos(angle - VIEW_ANGLE);
        double leftEndY = nextY + VIEW_DISTANCE * Math.sin(angle - VIEW_ANGLE);
        // Return true if either line intersects with a wall
        return intersectsWall(nextX, nextY, leftEndX, leftEndY, room);
    }

    private boolean isInViewOfEdgeRight(double nextX, double nextY, Room room) {
        // Calculate the endpoints of the visibility lines
        double rightEndX = nextX + VIEW_DISTANCE * Math.cos(angle + VIEW_ANGLE);
        double rightEndY = nextY + VIEW_DISTANCE * Math.sin(angle + VIEW_ANGLE);
        // Return true if either line intersects with a wall
        return intersectsWall(nextX, nextY, rightEndX, rightEndY, room);
    }

    private boolean isInViewOfEdgeCenter(double nextX, double nextY, Room room) {
        // Calculate the endpoint of the visibility line
        double centerEndX = nextX + VIEW_DISTANCE * Math.cos(angle);
        double centerEndY = nextY + VIEW_DISTANCE * Math.sin(angle);
        // Return true if the line intersects with a wall
        return intersectsWall(nextX, nextY, centerEndX, centerEndY, room);
    }

    private boolean intersectsWall(double startX, double startY, double endX, double endY, Room room) {
        // Check if the line segment (startX, startY) to (endX, endY) intersects with any wall
        // You can use line-segment intersection algorithms (e.g., Liang-Barsky) here
        // For simplicity, I'll assume it intersects if any endpoint is outside the room
        return startX < 0 || startX > room.getWidth() || startY < 0 || startY > room.getHeight() ||
                endX < 0 || endX > room.getWidth() || endY < 0 || endY > room.getHeight();
    }

}



//private boolean isInViewOfEdgeLeft(double nextX, double nextY, Room room) {
//    // Calculate the endpoints of the visibility lines
//    double leftEndX = nextX + VIEW_DISTANCE * Math.cos(angle) - VIEW_WIDTH * Math.sin(angle) / 2;
//    double leftEndY = nextY + VIEW_DISTANCE * Math.sin(angle) + VIEW_WIDTH * Math.cos(angle) / 2;
//
//    // Return true if either line intersects with a wall
//    return intersectsWall(nextX, nextY, leftEndX, leftEndY, room);
//}
//
//private boolean isInViewOfEdgeRight(double nextX, double nextY, Room room) {
//    // Calculate the endpoints of the visibility lines
//    double rightEndX = nextX + VIEW_DISTANCE * Math.cos(angle) + VIEW_WIDTH * Math.sin(angle) / 2;
//    double rightEndY = nextY + VIEW_DISTANCE * Math.sin(angle) - VIEW_WIDTH * Math.cos(angle) / 2;
//
//    // Return true if either line intersects with a wall
//    return intersectsWall(nextX, nextY, rightEndX, rightEndY, room);
//}
//
//private boolean intersectsWall(double startX, double startY, double endX, double endY, Room room) {
//    // Check if the line segment (startX, startY) to (endX, endY) intersects with any wall
//    // You can use line-segment intersection algorithms (e.g., Liang-Barsky) here
//    // For simplicity, I'll assume it intersects if any endpoint is outside the room
//    return startX < 0 || startX > room.getWidth() || startY < 0 || startY > room.getHeight() ||
//            endX < 0 || endX > room.getWidth() || endY < 0 || endY > room.getHeight();
//}