package org.openjfx.javaproject.ui.buttons;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import org.openjfx.javaproject.RobotSimulator;
import org.openjfx.javaproject.room.Autorobot;
import org.openjfx.javaproject.room.Position;
import org.openjfx.javaproject.room.Room;

import java.util.Optional;

public class AddRobotButton extends Button {

    public AddRobotButton(RobotSimulator simulator, Room room, Pane roomPane) {
        super("Add Robot");
        this.setOnAction(e -> {
            if (!simulator.isSimulationStarted()){
                // Create a dialog for user input
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Add Robot");
                dialog.setHeaderText("Enter the robot's position (x,y):");
                Optional<String> result = dialog.showAndWait();
                result.ifPresent(position -> {
                    String[] coordinates = position.split(",");
                    int x = Integer.parseInt(coordinates[0].trim());
                    int y = Integer.parseInt(coordinates[1].trim());
                    Autorobot newRobot = Autorobot.create(room, new Position(x,y), 0);

                    if (newRobot == null) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Dialog");
                        alert.setHeaderText("Robot Creation Error");
                        alert.setContentText("Robot could not be created at the specified position due to an obstacle.");
                        alert.showAndWait();
                    } else {
                        roomPane.getChildren().add(newRobot.getShape());
                    }
                });
            }
        });
    }
}
