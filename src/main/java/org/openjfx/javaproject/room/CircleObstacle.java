package org.openjfx.javaproject.room;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.openjfx.javaproject.common.Obstacle;

public class CircleObstacle extends Obstacle {
    private final Circle circle;

    public CircleObstacle(Position position, double radius) {
        super(position);
        circle = new Circle(position.getX(), position.getY(), radius);
        circle.setFill(Color.GRAY);
    }

    @Override
    public Circle getShape() {
        return circle;
    }

    @Override
    public double getSize() {
        return circle.getRadius();
    }
}
