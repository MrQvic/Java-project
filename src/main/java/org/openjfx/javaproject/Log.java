package org.openjfx.javaproject;
import java.util.ArrayList;
import java.util.List;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class Log {
    private List<List<String>> recordedPositions;

    public void initLogs(String filename) {
        recordedPositions = new ArrayList<>();

        try (FileWriter writer = new FileWriter(filename, false)) {

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    public List<String> getLogs(int timeStep) {
        if (timeStep >= 0 && timeStep < recordedPositions.size()) {
            return recordedPositions.get(timeStep);
        }
        return null;
    }

    public int getTotalTimeSteps() {
        return recordedPositions.size();
    }

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
