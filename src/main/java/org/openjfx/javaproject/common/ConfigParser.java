package org.openjfx.javaproject.common;
import org.openjfx.javaproject.room.Room;
import org.openjfx.javaproject.room.Autorobot;
import org.openjfx.javaproject.room.CircleObstacle;
import org.openjfx.javaproject.room.Position;
import org.openjfx.javaproject.room.ControlledRobot;
import org.openjfx.javaproject.room.RectangleObstacle;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileReader;
import java.util.List;
import java.io.IOException;

public class ConfigParser {

    /**
     * Parse config file and create room with all objects from the config file.
     *
     * @param filename Name and path of the configuration file.
     * @return Room object with size, robots, obstacles and controlled robot specified in the config file.
     */
    public static Room parse(String filename) {
        try (FileReader fileReader = new FileReader(filename)) {
            // Parse JSON
            JSONObject jsonObject = new JSONObject(new org.json.JSONTokener(fileReader));

            // Default size
            double roomX = 500;
            double roomY = 500;

            if (jsonObject.has("roomX")) {
                roomX = jsonObject.getDouble("roomX");
            }
            if (jsonObject.has("roomY")) {
                roomY = jsonObject.getDouble("roomY");
            }

            Room room = new Room(roomX, roomY);

            // Parse obstacles
            JSONArray obstacles = jsonObject.getJSONArray("obstacles");

            for (int i = 0; i < obstacles.length(); i++) {
                // Get obstacle and obstacle type
                JSONObject obstacle = obstacles.getJSONObject(i);
                String type = obstacle.getString("type");

                if(obstacle.has("x") && obstacle.has("y")){
                    Position position = new Position(obstacle.getDouble("x"), obstacle.getDouble("y"));
                    double size;
                    if(obstacle.has("size")){
                        size = obstacle.getDouble("size");
                    } else {
                        size = 20;
                    }

                    if ("circle".equals(type)) {
                        Obstacle new_obstacle = new CircleObstacle(position, size);
                        room.addObstacle(new_obstacle);
                    } else if ("rectangle".equals(type)) {
                        Obstacle new_obstacle = new RectangleObstacle(position, size);
                        room.addObstacle(new_obstacle);
                    }
                }
            }

            // Get auto robots
            JSONArray autoRobots = jsonObject.getJSONArray("autoRobots");

            for (int i = 0; i < autoRobots.length(); i++) {
                JSONObject robot = autoRobots.getJSONObject(i);

                // Check if robot has valid coordinates, if not then skip this one
                if(robot.has("x") && robot.has("y")){
                    // If angle is set, use it if not set it to zero.
                    double angle;
                    if(robot.has("angle")){
                        angle = robot.getDouble("angle");
                    } else {
                        angle = 0;
                    }
                    Position position = new Position(robot.getDouble("x"), robot.getDouble("y"));

                    Autorobot autorobot = Autorobot.create(room, position, angle);

                    if(autorobot != null){
                        room.addRobot(autorobot);
                    }
                }
            }

            // Get controlled robot
            JSONArray controlledRobot = jsonObject.getJSONArray("controlledRobot");
            for (int i = 0; i < controlledRobot.length(); i++) {
                JSONObject robot = controlledRobot.getJSONObject(i);
                if(robot.has("x") && robot.has("y")){
                    // If angle is set, use it if not set it to zero.
                    double angle;
                    if(robot.has("angle")){
                        angle = robot.getDouble("angle");
                    } else {
                        angle = 0;
                    }
                    Position position = new Position(robot.getDouble("x"), robot.getDouble("y"));

                    ControlledRobot new_robot = ControlledRobot.create(room, position, angle);

                    if(new_robot != null){
                        room.addControlledRobot(new_robot);
                    }
                }
            }
            return room;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Convert obstacles into JSON
     *
     * @param obstacles List of obstacles.
     * @return Information about obstacles in JSON format.
     */
    public static JSONArray obstaclesToJson(List<Obstacle> obstacles) {
        JSONArray jsonArray = new JSONArray();
        for (Obstacle obstacle : obstacles) {
            JSONObject obj = new JSONObject();
            obj.put("x", obstacle.getPosition().getX());
            obj.put("y", obstacle.getPosition().getY());
            obj.put("size", obstacle.getSize()/2);
            obj.put("type", obstacle.getType());
            jsonArray.put(obj);
        }
        return jsonArray;
    }

    /**
     * Convert self controlled robots into JSON
     *
     * @param autoRobots List of obstacles.
     * @return Information about self controlled robots in JSON format.
     */
    public static JSONArray autoRobotsToJson(List<Autorobot> autoRobots) {
        JSONArray jsonArray = new JSONArray();
        for (Autorobot autoRobot : autoRobots) {
            JSONObject obj = new JSONObject();
            obj.put("x", autoRobot.getPosition().getX());
            obj.put("y", autoRobot.getPosition().getY());
            obj.put("angle", autoRobot.getAngle());
            jsonArray.put(obj);
        }
        return jsonArray;
    }

    /**
     * Convert controlled robot information into JSON
     *
     * @param controlledRobot List of obstacles.
     * @return Information about the controlled robot in JSON format.
     */
    public static JSONArray robotsToJson(ControlledRobot controlledRobot) {
        JSONArray jsonArray = new JSONArray();
        JSONObject obj = new JSONObject();
        obj.put("x", controlledRobot.getPosition().getX());
        obj.put("y", controlledRobot.getPosition().getY());
        jsonArray.put(obj);
        return jsonArray;
    }

}
