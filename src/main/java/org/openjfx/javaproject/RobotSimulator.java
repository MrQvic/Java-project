package org.openjfx.javaproject;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.openjfx.javaproject.room.Autorobot;

import javafx.geometry.Pos;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;

import javafx.scene.control.Button;

import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import javafx.event.EventHandler;

import org.openjfx.javaproject.room.ControlledRobot;
import org.openjfx.javaproject.room.Position;
import org.openjfx.javaproject.room.Room;
import org.openjfx.javaproject.ui.buttons.*;

import java.util.ArrayList;
import java.util.List;

import javafx.stage.FileChooser;
import java.io.File;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.FileChooser;


public class RobotSimulator extends Application {
    private AnimationTimer timer;
    private boolean isSimulationStarted = false;
    private Pane roomPane;

    String logFile = "log.txt";

    @Override
    public void start(Stage primaryStage) {
        Room room = getRoom();

        // Create a dialog for input
        roomPane = room.create();
        roomPane.setStyle("-fx-background-color: #bdc3c7;");


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
        ConfigButton configButton = new ConfigButton();

        // Set Button Sizes
        addControlledRobotButton.setPrefSize(135,12);
        addObstacleButton.setPrefSize(135,12);
        addRobotButton.setPrefSize(135,12);
        startButton.setPrefSize(135,12);
        pauseButton.setPrefSize(135,12);

        // Create a new pane for top buttons
        VBox topButtonPane = new VBox(10);
        topButtonPane.setAlignment(Pos.TOP_CENTER);
        topButtonPane.getChildren().addAll(addObstacleButton, addControlledRobotButton, addRobotButton);
        topButtonPane.setPadding(new Insets(0, 10, 10, 10));

        // Create a new pane for bottom buttons
        VBox bottomButtonPane = new VBox(10);
        bottomButtonPane.setAlignment(Pos.BOTTOM_CENTER);
        bottomButtonPane.getChildren().addAll(startButton, pauseButton);
        bottomButtonPane.setPadding(new Insets(0, 10, 0, 10));

        // Create a new BorderPane for the button layout
        BorderPane buttonLayout = new BorderPane();
        buttonLayout.setTop(topButtonPane);
        buttonLayout.setBottom(bottomButtonPane);

        // Create a main pane and add roomPane and buttonPane
        BorderPane mainPane = new BorderPane();
        mainPane.setPadding(new Insets(10, 0, 10, 10)); // Set padding for mainPane
        mainPane.setCenter(roomPane);
        mainPane.setRight(buttonLayout);

        // Main Scene
        mainPane.setStyle("-fx-background-color: #2c3e50;");
        Scene scene = new Scene(mainPane, room.getWidth() + 150 + 13, room.getHeight() + 20);


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

        primaryStage.setResizable(false);
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


    private Room getRoom() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Room Configuration");
        dialog.setHeaderText("Choose your configuration method:");

        // Buttons
        ButtonType loadConfigButtonType = new ButtonType("Load Config");
        ButtonType inputSizeButtonType = new ButtonType("Input Size");
        dialog.getDialogPane().getButtonTypes().addAll(loadConfigButtonType, inputSizeButtonType);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == loadConfigButtonType) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
                File selectedFile = fileChooser.showOpenDialog(null);
                if (selectedFile != null) {
                    return selectedFile.getPath();
                }
            } else if (buttonType == inputSizeButtonType) {
                TextInputDialog sizeDialog = new TextInputDialog("500");
                sizeDialog.setTitle("Room Size Input");
                sizeDialog.setHeaderText("Enter Room Size:");
                sizeDialog.setContentText("Please enter room size:");
                Optional<String> result = sizeDialog.showAndWait();
                if (result.isPresent()){
                    return result.get();
                }
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();

        final Room[] room = {null};

        result.ifPresent(roomConfig -> {
            if (roomConfig.matches("\\d+")) {
                int roomSize = Integer.parseInt(roomConfig);
                room[0] = new Room(roomSize, roomSize);

            } else {
                String filePath = roomConfig;
                // Parse json here
            }
        });

        return room[0];
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

