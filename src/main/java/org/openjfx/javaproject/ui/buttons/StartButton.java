package org.openjfx.javaproject.ui.buttons;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Button;
import org.openjfx.javaproject.RobotSimulator;
import org.openjfx.javaproject.room.Autorobot;
import org.openjfx.javaproject.room.Room;

/**
 * A button to start the simulation.
 */

public class StartButton extends Button {

    public StartButton(RobotSimulator simulator) {
        super("Start Simulation");
        this.setOnAction(e -> simulator.startSimulation());
    }
}


