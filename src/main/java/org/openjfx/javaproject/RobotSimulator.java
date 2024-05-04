package org.openjfx.javaproject;
import org.openjfx.javaproject.room.Autorobot;

import javafx.scene.layout.StackPane;

import javafx.geometry.Pos;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;

import javafx.scene.control.Button;

import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import javafx.event.EventHandler;

import org.openjfx.javaproject.room.ControlledRobot;
import org.openjfx.javaproject.room.Position;
import org.openjfx.javaproject.room.Room;
import org.openjfx.javaproject.ui.buttons.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class RobotSimulator extends Application {
    private AnimationTimer timer;
    private boolean isSimulationStarted = false;
    private Pane roomPane;

    String logFile = "log.txt";

    @Override
    public void start(Stage primaryStage) {

        // Create a dialog for input
        Room room = getRoom();
        //Pane roomPane = room.create();
        roomPane = room.create();

        // Initialise logging
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
                if(room.isControlledRobotSet()){
                    room.controlledRobot.update(room);
                }
                stepNumber++;
                System.out.println(stepNumber);

                log.recordLogs(stepNumber, log.formatToJson(positions));
                if (stepNumber % 120 == 0) { // Output to file every 2 seconds
                    //log.bufferOut(logFile);
                }
            }
        };

        // Create Button Instances
        AddControlledRobotButton addControlledRobotButton = new AddControlledRobotButton(this, room, roomPane);
        AddRobotButton addRobotButton = new AddRobotButton(this, room, roomPane);
        Button addObstacleButton = new AddObstacleButton(this, room, roomPane);
        Button startButton = new StartButton(this);
        PauseButton pauseButton = new PauseButton(timer);

        // Set Button Sizes
        addControlledRobotButton.setPrefSize(135,12);
        addObstacleButton.setPrefSize(135,12);
        addRobotButton.setPrefSize(135,12);
        startButton.setPrefSize(135,12);
        pauseButton.setPrefSize(135,12);

        // Create a new pane for buttons
        VBox buttonPane = new VBox(10);
        buttonPane.setAlignment(Pos.TOP_CENTER);
        buttonPane.getChildren().addAll(startButton, pauseButton, addRobotButton, addObstacleButton, addControlledRobotButton);

        // Create a main pane and add roomPane and buttonPane
        BorderPane mainPane = new BorderPane();
        mainPane.setCenter(roomPane);
        mainPane.setRight(buttonPane);

        // Main Scene
        mainPane.setStyle("-fx-padding: 10px 10px 10px 10px;");
        Scene scene = new Scene(mainPane, room.getWidth() + 170, room.getHeight() + 20); // Added 150 for the width of buttonPane


        // Key Input Listeners
        scene.setOnKeyPressed(event -> {
            System.out.println("Key Pressed");
            if (room.isControlledRobotSet()) {
                room.controlledRobot.keyPressed(event);
            }
        });

        scene.setOnKeyReleased(event -> {
            System.out.println("Key Released");
            if (room.isControlledRobotSet()) {
                room.controlledRobot.keyReleased(event);
            }
        });


        primaryStage.setScene(scene);
        primaryStage.show();

        // Debugging: Check which node has the focus
        scene.focusOwnerProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                System.out.println("Focus is on: " + newValue.getClass().getSimpleName());
            } else {
                System.out.println("No focus");
            }
        });
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
        roomPane.requestFocus();
    }

    public boolean isSimulationStarted() {
        return isSimulationStarted;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

