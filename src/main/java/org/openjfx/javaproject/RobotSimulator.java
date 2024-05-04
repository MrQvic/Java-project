package org.openjfx.javaproject;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;

import javafx.scene.control.Button;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.openjfx.javaproject.room.Autorobot;

import org.openjfx.javaproject.room.Room;
import org.openjfx.javaproject.ui.buttons.AddObstacleButton;
import org.openjfx.javaproject.ui.buttons.AddRobotButton;
import org.openjfx.javaproject.ui.buttons.PauseButton;
import org.openjfx.javaproject.ui.buttons.StartButton;

public class RobotSimulator extends Application {
    private AnimationTimer timer;
    private boolean isSimulationStarted = false;

    @Override
    public void start(Stage primaryStage) {
        // Create a room
        Room room = new Room(500, 500);
        Pane roomPane = room.create();

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Update each robot
                for (Autorobot robot : room.getRobots()) {
                    robot.update(room);
                }
            }
        };

        // Create a start button
        Button startButton = new StartButton(this);

        // Create a pause button
        PauseButton pauseButton = new PauseButton(timer);

        // Create an add robot button
        AddRobotButton addRobotButton = new AddRobotButton(this, room, roomPane);

        Button addObstacleButton = new AddObstacleButton(this, room, roomPane);

        // Create a new pane for buttons
        VBox buttonPane = new VBox(10); // 10 is the spacing between buttons
        buttonPane.getChildren().addAll(startButton, pauseButton, addRobotButton, addObstacleButton);


        // Create a main pane and add roomPane and buttonPane
        BorderPane mainPane = new BorderPane();
        mainPane.setCenter(roomPane);
        mainPane.setRight(buttonPane);

        Scene scene = new Scene(mainPane, room.getWidth() + 150, room.getHeight()); // Added 150 for the width of buttonPane

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void startSimulation() {
        isSimulationStarted = true;
        timer.start();
    }

    public boolean isSimulationStarted() {
        return isSimulationStarted;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

