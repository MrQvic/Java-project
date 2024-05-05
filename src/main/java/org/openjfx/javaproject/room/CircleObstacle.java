package org.openjfx.javaproject.room;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.openjfx.javaproject.common.Obstacle;

/**
 * Represents a circular obstacle in a room.
 */
public class CircleObstacle extends Obstacle {
    private final Circle circle;

    /**
     * Constructs a CircleObstacle with the specified position and radius.
     *
     * @param position The position of the center of the circle.
     * @param radius The radius of the circle.
     */
    public CircleObstacle(Position position, double radius) {
        super(position);
        circle = new Circle(position.getX(), position.getY(), radius);
        circle.setFill(Color.GRAY);
    }

    /**
     * Retrieves the graphical representation of the CircleObstacle.
     *
     * @return The Circle representing the CircleObstacle.
     */
    @Override
    public Circle getShape() {
        return circle;
    }

    /**
     * Retrieves the size of the CircleObstacle, which is its radius.
     *
     * @return The radius of the CircleObstacle.
     */
    @Override
    public double getSize() {
        return circle.getRadius();
    }

    /**
     * Retrieves the type of the obstacle.
     *
     * @return The type of the obstacle, which is "circle".
     */
    @Override
    public String getType() {
        return "circle";
    }
}
