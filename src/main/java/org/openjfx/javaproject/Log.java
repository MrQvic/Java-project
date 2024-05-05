package org.openjfx.javaproject;
import java.util.ArrayList;
import java.util.List;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


/**
 * The Log class manages recording and buffering of simulation logs.
 */
public class Log {
    private List<List<String>> recordedPositions;

    /**
     * Initializes the log file.
     * @param filename The name of the log file.
     */
    public void initLogs(String filename) {
        recordedPositions = new ArrayList<>();

        try (FileWriter writer = new FileWriter(filename, false)) {

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Records the positions of robots at a given time step.
     * @param stepNumber The time step.
     * @param positions The list of positions of robots.
     */
    public void recordLogs(int stepNumber, List<String> positions) {
        StringBuilder lineBuilder = new StringBuilder("{\"step\": " + stepNumber + ", ");
        lineBuilder.append("\"robots\" : [");
        for (String position : positions) {
            lineBuilder.append(position).append(", ");
        }

        lineBuilder.setLength(lineBuilder.length() - 2);
        lineBuilder.append("]}\n");
        recordedPositions.add(List.of(lineBuilder.toString()));
    }

    /**
     * Retrieves the recorded logs at a specific time step.
     * @param timeStep The time step to retrieve logs for.
     * @return A list of positions of robots at the specified time step.
     */
    public List<String> getLogs(int timeStep) {
        if (timeStep >= 0 && timeStep < recordedPositions.size()) {
            return recordedPositions.get(timeStep);
        }
        return null;
    }

    /**
     * Gets the total number of time steps recorded in the log.
     * @return The total number of time steps.
     */
    public int getTotalTimeSteps() {
        return recordedPositions.size();
    }

    /**
     * Flushes recorded logs to a log file.
     * @param filename The name of the log file.
     */
    public void bufferOut(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) { // Append mode
            for (List<String> stepData : recordedPositions) {
                for (String line : stepData) {
                    writer.write(line);
                }
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        recordedPositions.clear();
    }

    /**
     * Formats the positions of robots to JSON format.
     * @param positions The list of positions to format.
     * @return A list of positions in JSON format.
     */
    public List<String> formatToJson(List<String> positions) {
        List<String> jsonPositions = new ArrayList<>();
        for (String position : positions) {
            String[] parts = position.split(" "); // Split by space
            String x = parts[0].replace(",", ".");
            String y = parts[1].replace(",", ".");
            String angle = parts[2].replace(",", ".");

            String jsonString = String.format("{\"x\": %s, \"y\": %s, \"angle\": %s}", x, y, angle);

            jsonPositions.add(jsonString);
        }
        return jsonPositions;
    }
}
