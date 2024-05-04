package org.openjfx.javaproject.ui.buttons;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import org.openjfx.javaproject.RobotSimulator;
import org.openjfx.javaproject.common.Obstacle;
import org.openjfx.javaproject.room.Position;
import org.openjfx.javaproject.room.Room;

import java.util.Optional;

public class AddObstacleButton extends Button {
    public AddObstacleButton(RobotSimulator simulator, Room room, Pane roomPane) {
        super("Add Obstacle");
        this.setOnAction(e -> {
            if (!simulator.isSimulationStarted()){

                // Create a dialog for user input
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Add Obstacle");
                dialog.setHeaderText("Enter the obstacle's position (x,y):");

                Optional<String> result = dialog.showAndWait();
                result.ifPresent(position -> {
                    String[] coordinates = position.split(",");
                    int x = Integer.parseInt(coordinates[0].trim());
                    int y = Integer.parseInt(coordinates[1].trim());

                    Obstacle obstacle = Obstacle.create(room, new Position(x,y), 30, "circle");
                    if (obstacle == null) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Dialog");
                        alert.setHeaderText("Obstacle Creation Error");
                        alert.setContentText("Obstacle could not be created at the specified position due to an obstacle.");

                        alert.showAndWait();
                    } else {
                        room.addObstacle(obstacle);
                        // Add the shape of the obstacle to the pane
                        roomPane.getChildren().add(obstacle.getShape());
                    }
                });

            }
        });
    }
}
