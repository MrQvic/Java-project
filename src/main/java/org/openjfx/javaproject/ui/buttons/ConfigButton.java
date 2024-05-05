package org.openjfx.javaproject.ui.buttons;

import javafx.scene.control.Button;
import org.openjfx.javaproject.common.ConfigParser;
import org.openjfx.javaproject.common.Obstacle;
import org.openjfx.javaproject.room.Room;
import org.openjfx.javaproject.room.Autorobot;
import org.openjfx.javaproject.room.ControlledRobot;
import org.openjfx.javaproject.room.RectangleObstacle;
import org.openjfx.javaproject.room.CircleObstacle;
import org.openjfx.javaproject.RobotSimulator;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;

public class ConfigButton extends Button {
    private Room room;
    public ConfigButton(Room room) {
        super("Save To Config");
        this.room = room;
        this.setOnAction(e -> saveToConfig());
    }

    public static void saveJSONToFile(int roomX, int roomY, List<Obstacle> obstacles, List<Autorobot> autoRobots, ControlledRobot controlledRobot, String filePath) {
        JSONObject json = new JSONObject();
        json.put("roomX", roomX);
        json.put("roomY", roomY);
        json.put("obstacles", ConfigParser.obstaclesToJson(obstacles));
        json.put("autoRobots", ConfigParser.autoRobotsToJson(autoRobots));
        json.put("controlledRobot", ConfigParser.robotsToJson(controlledRobot));

        try (FileWriter file = new FileWriter(filePath)) {
            file.write(json.toString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToConfig() {
        TextInputDialog dialog = new TextInputDialog("config.json");
        dialog.setTitle("Save to Config");
        dialog.setHeaderText("Enter the config file name:");
        dialog.setContentText("Name:");

        Optional<String> result = dialog.showAndWait();
        String path = result.orElse("config.json");
        saveJSONToFile((int) room.getWidth(), (int) room.getHeight(), room.getObstacles(), room.getRobots(), room.getControlledRobot(), path);
        //Parse into config HERE
        //result.ifPresent(name -> System.out.println("Saved to config file: " + name));
    }
}

