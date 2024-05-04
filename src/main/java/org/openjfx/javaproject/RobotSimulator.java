package org.openjfx.javaproject;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;

import javafx.scene.control.Button;

import javafx.scene.control.TextInputDialog;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RobotSimulator extends Application {
    private AnimationTimer timer;
    private boolean isSimulationStarted = false;

    String logFile = "log.txt";

    @Override
    public void start(Stage primaryStage) {

        // Create a dialog for input
        Room room = getRoom();
        Pane roomPane = room.create();

        Log log = new Log();
        log.initLogs(logFile);



        timer = new AnimationTimer() {
            int stepNumber = 0;
            @Override
            public void handle(long now) {
                // Update each robot
                List<String> positions = new ArrayList<>();

                for (Autorobot robot : room.getRobots()) {
                    robot.update(room);
                    System.out.println("ROBOT\n");
                    positions.add(robot.getPositionAsString());
                }
                stepNumber++;
                System.out.println(stepNumber);

                log.recordLogs(stepNumber, log.formatToJson(positions));
                if (stepNumber % 120 == 0) { // Output to file every 2 seconds
                    log.bufferOut(logFile);
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

    private static Room getRoom() {
        TextInputDialog dialog = new TextInputDialog("500");
        dialog.setTitle("Room Size Input");
        dialog.setHeaderText("Enter Room Size:");
        dialog.setContentText("Please enter room size:");
        Optional<String> result = dialog.showAndWait();
        int roomSize = 500; // default value
        if (result.isPresent()){
            roomSize = Integer.parseInt(result.get());
        }
        // Create a room with user input size
        return new Room(roomSize, roomSize);
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

