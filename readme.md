# Simple 2D Robot Simulator Project (IJA 2023/24)

## Project Overview
This project is a simple 2D simulation of robots in a bounded rectangular environment with obstacles.
It features autonomous and remotely controlled robots that navigate around square obstacles, avoiding collisions and responding to user inputs.
Placement of obstacles and robots can be set interactively in the GUI, saved to a file and loaded from a file.
Obstacles and robots can be removed by clicking on them with mouse.

![obrazek](https://github.com/MrQvic/Java-project/assets/101328994/6282573d-cddf-436f-872d-0c1f1573d68a)


### Team Members
- Adam Mrkva    - xmrkva04
- Daniel Jacobs - xjacob00

## Running the release JAR
After downloading the JAR file, you can run the application using the following command:

    java -jar Java-project-1.0.jar

## Compilation and Execution From Source
This application is built and managed using Maven. To compile and run the project, follow these steps:

### Prerequisites
Ensure that you have Maven installed and correctly setup on your system.

### Compiling the Application
1. Open a terminal or command prompt.
2. Navigate to the root directory of the project where the `pom.xml` file is located.
3. Run the following command to compile the project:

    `mvn clean compile`

### Generating javadocs

    mvn javadoc:javadoc

### Running the Application
After successfully compiling the project, you can run the application using the following command:

    mvn javafx:run

This will start the simulation GUI, where you can interact with the robots and the environment.
