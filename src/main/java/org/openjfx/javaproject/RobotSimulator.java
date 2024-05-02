package org.openjfx.javaproject;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class RobotSimulator extends Application {
    private List<Autorobot> robots;

    @Override
    public void start(Stage primaryStage) {
        // Create a room
        Room room = new Room(500, 500);
        Pane root = room.create();

        // Create robots
        robots = new ArrayList<>();
        robots.add(new Autorobot(100, 100));
        robots.add(new Autorobot(200, 200));
        robots.add(new Autorobot(300, 300));
        // Add more robots as needed

        // Add robots to the room
        for (Autorobot robot : robots) {
            root.getChildren().add(robot.getShape());
        }

        Scene scene = new Scene(root, room.getWidth(), room.getHeight());

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Update each robot
                for (Autorobot robot : robots) {
                    robot.update(room);
                }
            }
        }.start();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
