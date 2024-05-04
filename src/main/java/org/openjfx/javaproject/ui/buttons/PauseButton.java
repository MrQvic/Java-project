package org.openjfx.javaproject.ui.buttons;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Button;

public class PauseButton extends Button {
    private final AnimationTimer timer;

    public PauseButton(AnimationTimer timer) {
        super("Pause Simulation");
        this.timer = timer;
        this.setOnAction(e -> this.timer.stop());
    }
}
