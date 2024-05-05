package org.openjfx.javaproject;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.openjfx.javaproject.room.Autorobot;
import org.openjfx.javaproject.common.Obstacle;

import org.openjfx.javaproject.common.ConfigParser;

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
import java.util.Optional;
import org.json.JSONObject;


public class RobotSimulator extends Application {
    private AnimationTimer timer;
    private boolean isSimulationStarted = false;
    private Pane roomPane;

    String logFile = "log.txt";

    @Override
    public void start(Stage primaryStage) {

        //ConfigParser.parse("C:\\Users\\Admin\\Documents\\GitHub\\Java\\Java-project\\src\\main\\java\\org\\openjfx\\javaproject\\common\\config.json");
        // Create a dialog for input
        //Room room = getRoom();
        Room room = ConfigParser.parse("C:\\Users\\Admin\\Documents\\GitHub\\Java\\Java-project\\src\\main\\java\\org\\openjfx\\javaproject\\common\\config.json");

        roomPane = room.create();
        roomPane.setStyle("-fx-background-color: #bdc3c7;");

        for (Autorobot robot : room.getRobots()) {
            roomPane.getChildren().add(robot.getShape());
        }
        for (Obstacle obstacle : room.getObstacles()) {
            roomPane.getChildren().add(obstacle.getShape());
        }

        roomPane.getChildren().add(room.getControlledRobot().getShape());
        roomPane.getChildren().add(room.getControlledRobot().getDirectionLine());


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

