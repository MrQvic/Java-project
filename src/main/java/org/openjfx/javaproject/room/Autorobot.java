package org.openjfx.javaproject.room;

import javafx.scene.robot.Robot;
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
        //DEBUG
        //for (Autorobot robot : room.getRobots()) {
        //    System.out.println("New robot added at position: " + robot.getPosition());
        //}
        // Calculate velocity vector components
        double velX = SPEED * Math.cos(angle);
        double velY = SPEED * Math.sin(angle);

        // Predict next position
        double nextX = position.getX() + velX * TIME_STEP;
        double nextY = position.getY() + velY * TIME_STEP;

        for (Obstacle obstacle : room.getObstacles()) {
            double distanceToObstacle = Math.hypot(position.getX() - obstacle.getPosition().getX(), position.getY() - obstacle.getPosition().getY());
            if (distanceToObstacle < RADIUS + 30  + SAFE_ZONE) { //TODO: 30 is radius of the obstacle!!!!!!!
                // Avoid the obstacle
                angle += 0.1;
            }
        }

        for (Autorobot robot : room.getRobots()){
            if (this != robot){
                //autobot x autbot collision logic
                //checkCollision(robot);
            }
        }

        if(room.isControlledRobotSet()){
            ControlledRobot controlledRobot = room.getControlledRobot();
            if (controlledRobot != null && checkCollision(controlledRobot, nextX, nextY)) {
                // If collision with the controlled robot, turn away
                //angle += 0.1;
                //return;
                // Calculate vector from this robot to the controlled robot
                double dx = controlledRobot.getPosition().getX() - position.getX();
                double dy = controlledRobot.getPosition().getY() - position.getY();

                // Calculate the angle away from the controlled robot
                double angleAway = Math.atan2(-dy, -dx);

                // Update the robot's angle to move away from the controlled robot
                angle = angleAway;

                // Move in the new direction
                velX = SPEED * Math.cos(angleAway);
                velY = SPEED * Math.sin(angleAway);
                nextX = position.getX() + velX * TIME_STEP;
                nextY = position.getY() + velY * TIME_STEP;
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
        if (isNearEdge(nextX, nextY, room)) {
            angle += 0.1;
        }
        // Update position
        position.setX(nextX);
        position.setY(nextY);
        updatePosition();


    }

    private boolean isNearEdge(double nextX, double nextY, Room room) {
        double leftEdge = RADIUS + SAFE_ZONE;
        double rightEdge = room.getWidth() - RADIUS - SAFE_ZONE;
        double topEdge = RADIUS + SAFE_ZONE;
        double bottomEdge = room.getHeight() - RADIUS - SAFE_ZONE;

        return nextX < leftEdge || nextX > rightEdge || nextY < topEdge || nextY > bottomEdge;
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

    private boolean checkCollision(Autorobot robot) {
        return false;
    }

    private boolean checkCollision(ControlledRobot robot, double nextX, double nextY) {
        double dx = nextX - robot.getPosition().getX();
        double dy = nextY - robot.getPosition().getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        return distance < (RADIUS + robot.getSize() + 10);
    }
}




//private boolean isInViewOfEdge(double nextX, double nextY, Room room) {
//    // Calculate the corners of the view rectangle
//    double leftX = nextX + VIEW_DISTANCE * Math.cos(angle) - VIEW_WIDTH * Math.sin(angle) / 2;
//    double leftY = nextY + VIEW_DISTANCE * Math.sin(angle) + VIEW_WIDTH * Math.cos(angle) / 2;
//    double rightX = nextX + VIEW_DISTANCE * Math.cos(angle) + VIEW_WIDTH * Math.sin(angle) / 2;
//    double rightY = nextY + VIEW_DISTANCE * Math.sin(angle) - VIEW_WIDTH * Math.cos(angle) / 2;
//
//    // Check if any of the corners are outside the room
//    return leftX < 0 || rightX > room.getWidth() || leftY < 0 || rightY > room.getHeight();
//}