package org.openjfx.javaproject.ui.buttons;

import javafx.scene.control.Button;
import org.openjfx.javaproject.RobotSimulator;
import org.openjfx.javaproject.room.Room;

public class ResetButton extends Button {
    public ResetButton(RobotSimulator environment, Room room){
        super("Clear all");
    }
}
