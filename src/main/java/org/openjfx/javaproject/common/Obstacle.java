package org.openjfx.javaproject.common;

import javafx.scene.shape.Shape;
import org.openjfx.javaproject.room.CircleObstacle;
import org.openjfx.javaproject.room.Position;
import org.openjfx.javaproject.room.RectangleObstacle;
import org.openjfx.javaproject.room.Room;


public abstract class Obstacle {
    protected final Position position;

    public Obstacle(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public abstract Shape getShape();
    public abstract double getSize();

    public static Obstacle create(Room room, Position position, double size, String type) {
        if (!room.canCreate(position, size)) {    //there is obstacle
            return null;
        }
        if (type.equals("circle")) {
            return new CircleObstacle(position, size);
        } else if (type.equals("rectangle")) {
            return new RectangleObstacle(position, size);
        } else {
            return null;
        }
    }
}
