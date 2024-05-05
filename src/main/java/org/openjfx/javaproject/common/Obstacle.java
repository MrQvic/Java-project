package org.openjfx.javaproject.common;

import javafx.scene.shape.Shape;
import org.openjfx.javaproject.room.CircleObstacle;
import org.openjfx.javaproject.room.Position;
import org.openjfx.javaproject.room.RectangleObstacle;
import org.openjfx.javaproject.room.Room;


public abstract class Obstacle {
    protected final Position position;

    /**
     * Construct obstacle on position
     *
     * @param position List of obstacles.
     */
    public Obstacle(Position position) {
        this.position = position;
    }

    /**
     * Get position of obstacle
     *
     * @return Positional information about the obstacle
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Returns a Shape object.
     *
     * @return A Shape object.
     */
    public abstract Shape getShape();

    /**
     * Returns the size of the shape.
     *
     * @return The size of the shape.
     */
    public abstract double getSize();

    /**
     * Returns the type of the shape.
     *
     * @return The type of the shape.
     */
    public abstract String getType();

    /**
     * Returns a Shape object.
     * @param room specifies the room in which the obstacle will be created.
     * @param position is the position of the obstacle.
     * @param size is the size of the obstacle.
     * @param type represents the type of the obstacle, it can be either "circle" or "rectangle"
     * @return Obstacle class instance or null if obstacle can not be created.
     */
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
