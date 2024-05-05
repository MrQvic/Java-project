package org.openjfx.javaproject.room;

import javafx.scene.shape.Circle;
import org.openjfx.javaproject.common.Obstacle;

public class Autorobot {
    private static final double TIME_STEP = 0.016; // 60 FPS
    private static final double SPEED = 50; // pixels per second
    private static final double RADIUS = 10; // radius of the robot

    private static final double SAFE_ZONE = 10; // distance from the edge

    private static final double VIEW_DISTANCE = 75; // how far the robot can see

    private static final double VIEW_ANGLE = Math.PI / 6; // 30 degrees

    private final Position position;
    private double angle;
    private final Circle shape;

    private Autorobot(Position position, double angle) {
        this.position = position;
        this.angle = angle;
        this.shape = new Circle(RADIUS);
        updatePosition();
    }

    public static Autorobot create(Room room, Position position, double angle) {
        if (!room.canCreate(position, RADIUS)) {    //there is obstacle
            return null;
        }
        Autorobot robot = new Autorobot(position, angle);
        room.addRobot(robot);
        return robot;
    }

    public void update(Room room) {
        boolean hasCollision = false;

        // Next Vector
        double velX = SPEED * Math.cos(angle);
        double velY = SPEED * Math.sin(angle);

        // Next position
        double nextX = position.getX() + velX * TIME_STEP;
        double nextY = position.getY() + velY * TIME_STEP;

        checkCollisionsWithObstacles(room, nextX, nextY);

        if (checkCollisionWithEdge(nextX, nextY, room)) {
            // Změnit směr
            angle += 0.2;
            hasCollision = true;
        }

        for (Autorobot otherRobot : room.getRobots()){
            if (this != otherRobot && checkCollision(otherRobot, nextX, nextY)) {

                double dx = otherRobot.getPosition().getX() - position.getX();
                double dy = otherRobot.getPosition().getY() - position.getY();

                angle = Math.atan2(-dy, -dx);

                nextX = position.getX() + SPEED * Math.cos(angle) * TIME_STEP;
                nextY = position.getY() + SPEED * Math.sin(angle) * TIME_STEP;
                hasCollision = true;
            }
        }

        if (!hasCollision) {
            if(room.isControlledRobotSet()){
                ControlledRobot controlledRobot = room.getControlledRobot();
                if (controlledRobot != null && checkCollision(controlledRobot, nextX, nextY)) {
                    double dx = controlledRobot.getPosition().getX() - position.getX();
                    double dy = controlledRobot.getPosition().getY() - position.getY();

                    // Escape angle
                    double angleAway = Math.atan2(-dy, -dx);

                    // Set escape angle
                    angle = angleAway;

                    // RUN AWAY
                    velX = SPEED * Math.cos(angleAway);
                    velY = SPEED * Math.sin(angleAway);
                    nextX = position.getX() + velX * TIME_STEP;
                    nextY = position.getY() + velY * TIME_STEP;

                    if(checkCollisionWithEdge(nextX,nextY,room) || checkCollisionsWithObstacles(room,nextX,nextY)){
                        hasCollision = true;
                    }
                }
            }
        }
        if(isInViewOfEdgeCenter(nextX, nextY, room)){
            angle += 0.1;
        } else if (isInViewOfEdgeLeft(nextX, nextY, room)){
            angle += 0.1;
        } else if (isInViewOfEdgeRight(nextX, nextY, room)) {
            angle -= 0.1;
        }

        // Update position
        if(!hasCollision){
            position.setX(nextX);
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
        return startX < 0 || startX > room.getWidth() || startY < 0 || startY > room.getHeight() ||
                endX < 0 || endX > room.getWidth() || endY < 0 || endY > room.getHeight();
    }

    public Position getPosition() {
        return position;
    }

    public String getPositionAsString() {
        String result = String.format("%.2f %.2f %.2f", position.getX(), position.getY(), getAngle());
        System.out.println(result);
        return result;

        //return "x: " + position.getX() + ", y: " + position.getY();
    }

    public double getSize() {
        return RADIUS;
    }
    public double getAngle(){ return angle; }

    // Override collision detection methods
    private boolean checkCollision(ControlledRobot robot, double nextX, double nextY) {
        double dx = nextX - robot.getPosition().getX();
        double dy = nextY - robot.getPosition().getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        return distance < (RADIUS + robot.getSize() + SAFE_ZONE);
    }

    private boolean checkCollision(Autorobot robot, double nextX, double nextY) {
        double dx = nextX - robot.getPosition().getX();
        double dy = nextY - robot.getPosition().getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        return distance < (RADIUS + robot.getSize() + SAFE_ZONE);
    }

    private boolean checkCollisionObstacle(Obstacle obstacle, double nextX, double nextY) {
        if (obstacle instanceof CircleObstacle circleObstacle) {
            double dx = nextX - circleObstacle.getPosition().getX();
            double dy = nextY - circleObstacle.getPosition().getY();
            double distance = Math.sqrt(dx * dx + dy * dy);
            return distance < (RADIUS + circleObstacle.getSize() + SAFE_ZONE);
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

    private boolean checkCollisionWithEdge(double nextX, double nextY, Room room) {
        if (nextX - RADIUS < 0 || nextX + RADIUS > room.getWidth()) {;
            return true;
        }
        return nextY - RADIUS < 0 || nextY + RADIUS > room.getHeight();
    }

    private boolean checkCollisionsWithObstacles(Room room, double nextX, double nextY) {
        for (Obstacle obstacle : room.getObstacles()) {
            if (checkCollisionObstacle(obstacle, nextX, nextY)) {
                double dx, dy;
                // Calculate angle to obstacle
                if (obstacle instanceof RectangleObstacle squareObstacle) {
                    // For squares, calculate the angle based on the nearest corner
                    double halfSize = squareObstacle.getSize() / 2;
                    double left = squareObstacle.getPosition().getX() - halfSize;
                    double right = squareObstacle.getPosition().getX() + halfSize;
                    double top = squareObstacle.getPosition().getY() - halfSize;
                    double bottom = squareObstacle.getPosition().getY() + halfSize;
                    dx = (nextX < left ? left : (Math.min(nextX, right))) - position.getX();
                    dy = (nextY < top ? top : (Math.min(nextY, bottom))) - position.getY();
                } else {
                    dx = obstacle.getPosition().getX() - position.getX();
                    dy = obstacle.getPosition().getY() - position.getY();
                }
                double angleToObstacle = Math.atan2(dy, dx);
                // Change direction
                angle = angleToObstacle + Math.PI / 2; // 90 degrees
                return true;
            }
        }
        return false;
    }
}