package org.openjfx.javaproject.ui.buttons;

import javafx.scene.control.Button;
import org.openjfx.javaproject.RobotSimulator;

import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;

public class ConfigButton extends Button {
    public ConfigButton() {
        super("Save To Config");
        this.setOnAction(e -> saveToConfig());
    }

    private void saveToConfig() {
        TextInputDialog dialog = new TextInputDialog("config");
        dialog.setTitle("Save to Config");
        dialog.setHeaderText("Enter the config file name:");
        dialog.setContentText("Name:");

        Optional<String> result = dialog.showAndWait();
        //Parse into config HERE
        //result.ifPresent(name -> System.out.println("Saved to config file: " + name));
    }
}

