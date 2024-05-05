package org.openjfx.javaproject.ui.buttons;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import org.openjfx.javaproject.RobotSimulator;
import org.openjfx.javaproject.room.Autorobot;
import org.openjfx.javaproject.room.ControlledRobot;
import org.openjfx.javaproject.room.Position;
import org.openjfx.javaproject.room.Room;

import java.util.Optional;

public class AddControlledRobotButton extends Button {

    public AddControlledRobotButton(RobotSimulator simulator, Room room, Pane roomPane) {
        super("Add Controlled Robot");
        this.setOnAction(e -> {
            if (!simulator.isSimulationStarted() && !room.isControlledRobotSet()){
                // Create a dialog for user input
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Add Controlled Robot");
                dialog.setHeaderText("Enter the controlled robot's position \"x,y\":");
                Optional<String> result = dialog.showAndWait();
                result.ifPresent(position -> {
                    String[] coordinates = position.split(",");
                    int x = Integer.parseInt(coordinates[0].trim());
                    int y = Integer.parseInt(coordinates[1].trim());
                    ControlledRobot newRobot = ControlledRobot.create(room, new Position(x,y), 0);

                    if (newRobot == null) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Dialog");
                        alert.setHeaderText("Robot Creation Error");
                        alert.setContentText("Robot could not be created at the specified position due to an obstacle.");
                        alert.showAndWait();
                    } else {
                        room.addControlledRobot(newRobot);
                        roomPane.getChildren().add(room.controlledRobot.getShape());
                        roomPane.getChildren().add(room.controlledRobot.getDirectionLine());

                        // Add event handlers for the robot's shape and direction line
                        room.controlledRobot.getShape().setOnMouseClicked(ev -> {
                            roomPane.getChildren().remove(room.controlledRobot.getShape());
                            roomPane.getChildren().remove(room.controlledRobot.getDirectionLine());
                            room.controlledRobot = null;
                        });

                        System.out.println("Requesting focus for controlled robot shape");
                        roomPane.requestFocus(); // Request focus on the roomPane
                    }
                });
            }
        });
    }
}
