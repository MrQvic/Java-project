package org.openjfx.javaproject.room;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.openjfx.javaproject.common.Obstacle;

public class RectangleObstacle extends Obstacle {
    private final Rectangle rectangle;

    public RectangleObstacle(Position position, double size) {
        super(position);
        size *= 2;
        // Adjust the position so the rectangle is created from the middle
        rectangle = new Rectangle(position.getX() - size / 2, position.getY() - size / 2, size, size);
        rectangle.setFill(Color.GRAY);
    }

    @Override
    public Rectangle getShape() {
        return rectangle;
    }

    @Override
    public double getSize() { //TODO: return correct value, not
        return rectangle.getHeight();
    }
}
