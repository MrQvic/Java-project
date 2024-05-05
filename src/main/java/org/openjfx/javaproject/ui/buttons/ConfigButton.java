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

/**
 * A button used to save the current configuration of the simulation to a JSON config file.
 */
public class ConfigButton extends Button {
    private Room room;

    /**
     * Constructs a ConfigButton.
     *
     * @param room The Room instance representing the simulation room.
     */
    public ConfigButton(Room room) {
        super("Save To Config");
        this.room = room;
        this.setOnAction(e -> saveToConfig());
    }

    /**
     * Saves the simulation configuration to a JSON file.
     *
     * @param roomX           The width of the simulation room.
     * @param roomY           The height of the simulation room.
     * @param obstacles       The list of obstacles in the simulation.
     * @param autoRobots      The list of autonomous robots in the simulation.
     * @param controlledRobot The controlled robot in the simulation.
     * @param filePath        The path to save the JSON file.
     */
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

    /**
     * Opens a dialog to enter the config file name and saves the configuration to the specified file.
     */
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

