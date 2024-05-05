package org.openjfx.javaproject.ui.buttons;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import org.openjfx.javaproject.RobotSimulator;
import org.openjfx.javaproject.room.Room;

/**
 * A button to clear all elements from the room and reset the simulation.
 */

public class ResetButton extends Button {
    public ResetButton(RobotSimulator simulation, Room room, Pane roomPane){
        super("Clear all");
        this.setOnAction(e -> {
            room.clearAll();
            roomPane.getChildren().clear();
            roomPane.getChildren().add(room.create());
            simulation.resetTimer();
        });

    }
}
