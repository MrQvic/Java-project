package org.openjfx.javaproject;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.Optional;

public class RobotSimulator extends Application {
    private AnimationTimer timer;

    @Override
    public void start(Stage primaryStage) {
        // Create a room
        Room room = new Room(500, 500);
        Pane roomPane = room.create();

        // Create a start button
        Button startButton = new Button("Start Simulation");
        startButton.setOnAction(e -> {
            if (timer == null) {
                timer = new AnimationTimer() {
                    @Override
                    public void handle(long now) {
                        // Update each robot
                        for (Autorobot robot : room.getRobots()) {
                            robot.update(room);
                        }
                    }
                };
            }
            timer.start();
        });

        // Create a pause button
        Button pauseButton = new Button("Pause Simulation");
        pauseButton.setOnAction(e -> {
            if (timer != null) {
                timer.stop();
            }
        });

        // Create an add robot button
        Button addRobotButton = new Button("Add Robot");
        addRobotButton.setOnAction(e -> {
            if (timer == null){
                // Create a dialog for user input
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Add Robot");
                dialog.setHeaderText("Enter the robot's position (x,y):");

                Optional<String> result = dialog.showAndWait();
                result.ifPresent(position -> {
                    String[] coordinates = position.split(",");
                    int x = Integer.parseInt(coordinates[0].trim());
                    int y = Integer.parseInt(coordinates[1].trim());

                    Autorobot newRobot = new Autorobot(x, y);
                    room.addRobot(newRobot);
                    roomPane.getChildren().add(newRobot.getShape());
                });
            }
        });

        Button addObstacleButton = new Button("Add Obstacle");
        addObstacleButton.setOnAction(e -> {
            if (timer == null){
                // Create a dialog for user input
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Add Obstacle");
                dialog.setHeaderText("Enter the obstacle's position (x,y):");

                Optional<String> result = dialog.showAndWait();
                result.ifPresent(position -> {
                    String[] coordinates = position.split(",");
                    int x = Integer.parseInt(coordinates[0].trim());
                    int y = Integer.parseInt(coordinates[1].trim());

                    Circle obstacle = new Circle(x, y, 30);
                    obstacle.setFill(Color.GRAY);

                    room.addObstacle(obstacle);
                    roomPane.getChildren().add(obstacle);
                });
            }
        });

        // Create a new pane for buttons
        VBox buttonPane = new VBox(10); // 10 is the spacing between buttons
        buttonPane.getChildren().addAll(startButton, pauseButton, addRobotButton, addObstacleButton);


        // Create a main pane and add roomPane and buttonPane
        BorderPane mainPane = new BorderPane();
        mainPane.setCenter(roomPane);
        mainPane.setRight(buttonPane);

        Scene scene = new Scene(mainPane, room.getWidth() + 150, room.getHeight()); // Added 100 for the width of buttonPane

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

