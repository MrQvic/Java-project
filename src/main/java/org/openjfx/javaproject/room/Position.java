package org.openjfx.javaproject.room;

import java.util.Objects;

public class Position {
    private double x;
    private double y;

    /**
     * Constructs a Position object with the specified x and y coordinates.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Retrieves the x-coordinate of this position.
     *
     * @return The x-coordinate.
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the x-coordinate of this position.
     *
     * @param x The new x-coordinate.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Retrieves the y-coordinate of this position.
     *
     * @return The y-coordinate.
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the y-coordinate of this position.
     *
     * @param y The new y-coordinate.
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Checks if this position is equal to another position.
     *
     * @param obj The object to compare with.
     * @return True if the positions are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Position position = (Position) obj;
        return Double.compare(position.x, x) == 0 &&
                Double.compare(position.y, y) == 0;
    }

    /**
     * Computes the hash code of this position.
     *
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Checks if this position is near another position within a specified threshold.
     *
     * @param other The other position.
     * @param threshold The maximum distance allowed for positions to be considered "near".
     * @return True if this position is near the other position, false otherwise.
     */
    public boolean isNear(Position other, double threshold) {
        double distance = Math.sqrt(Math.pow((x - other.x), 2) + Math.pow((y - other.y), 2));
        return distance <= threshold;
    }

}

