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

/**
 * A button used to add obstacles to the simulation room.
 */
public class AddObstacleButton extends Button {

    /**
     * Constructs an AddObstacleButton.
     *
     * @param simulator The RobotSimulator instance managing the simulation.
     * @param room      The Room instance representing the simulation room.
     * @param roomPane  The Pane where the simulation room is displayed.
     */
    public AddObstacleButton(RobotSimulator simulator, Room room, Pane roomPane) {
        super("Add Obstacle");
        this.setOnAction(e -> {
            if (!simulator.isSimulationStarted()){

                // Create a series of dialog boxes for user input
                TextInputDialog positionDialog = new TextInputDialog();
                positionDialog.setTitle("Add Obstacle");
                positionDialog.setHeaderText("Enter the obstacle's position \"x,y\":");

                TextInputDialog typeDialog = new TextInputDialog("rectangle");
                typeDialog.setTitle("Add Obstacle");
                typeDialog.setHeaderText("Enter the obstacle's type (rectangle or circle):");

                TextInputDialog sizeDialog = new TextInputDialog("30");
                sizeDialog.setTitle("Add Obstacle");
                sizeDialog.setHeaderText("Enter the obstacle's size:");

                Optional<String> positionResult = positionDialog.showAndWait();
                Optional<String> typeResult = typeDialog.showAndWait();
                Optional<String> sizeResult = sizeDialog.showAndWait();

                if (positionResult.isPresent() && typeResult.isPresent() && sizeResult.isPresent()) {
                    String[] coordinates = positionResult.get().split(",");
                    int x = Integer.parseInt(coordinates[0].trim());
                    int y = Integer.parseInt(coordinates[1].trim());
                    String type = typeResult.get();
                    int size = Integer.parseInt(sizeResult.get());

                    Obstacle obstacle = Obstacle.create(room, new Position(x,y), size, type);
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

                        // Add event handler for the obstacle's shape
                        obstacle.getShape().setOnMouseClicked(event -> {
                            room.getObstacles().remove(obstacle);
                            roomPane.getChildren().remove(obstacle.getShape());
                        });
                    }
                }
            }
        });
    }
}


