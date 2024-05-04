package org.openjfx.javaproject.room;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.openjfx.javaproject.common.Obstacle;

public class RectangleObstacle extends Obstacle {
    private final Rectangle rectangle;

    public RectangleObstacle(Position position) {
        super(position);
        rectangle = new Rectangle(position.getX(), position.getY(), 60, 60);
        rectangle.setFill(Color.GRAY);
    }

    @Override
    public Rectangle getShape() {
        return rectangle;
    }

    @Override
    public double getSize() { //TODO: return correct value, not
        return rectangle.getWidth() * rectangle.getHeight();
    }
}
