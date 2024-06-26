package org.openjfx.javaproject.ui.buttons;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Button;
import org.openjfx.javaproject.RobotSimulator;

/**
 * A button to pause the simulation.
 */

public class PauseButton extends Button {
    public PauseButton(RobotSimulator simulation) {
        super("Pause Simulation");
        this.setOnAction(e -> {
            simulation.resetTimer();
        });
    }
}
