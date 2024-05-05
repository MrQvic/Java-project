package org.openjfx.javaproject.room;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.openjfx.javaproject.common.Obstacle;

public class RectangleObstacle extends Obstacle {
    private final Rectangle rectangle;

    /**
     * Constructs a RectangleObstacle with the specified position and size.
     *
     * @param position The position of the center of the rectangle.
     * @param size The size (width and height) of the rectangle.
     */
    public RectangleObstacle(Position position, double size) {
        super(position);
        size *= 2;
        // Adjust the position so the rectangle is created from the middle
        rectangle = new Rectangle(position.getX() - size / 2, position.getY() - size / 2, size, size);
        rectangle.setFill(Color.GRAY);
    }

    /**
     * Retrieves the JavaFX Rectangle representing this obstacle's shape.
     *
     * @return The JavaFX Rectangle.
     */
    @Override
    public Rectangle getShape() {
        return rectangle;
    }

    /**
     * Retrieves the size of this obstacle.
     *
     * @return The size of the obstacle.
     */
    @Override
    public double getSize() { //TODO: return correct value, not
        return rectangle.getHeight();
    }

    /**
     * Retrieves the type of this obstacle.
     *
     * @return The type of the obstacle.
     */
    @Override
    public String getType() {
        return "rectangle";
    }
}
