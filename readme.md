# Simple 2D Robot Simulator Project (IJA 2023/24)

## Project Overview
This project is a simple 2D simulation of robots within a bounded rectangular environment with obstacles.
It features autonomous and remotely controlled robots that navigate around static square obstacles, avoiding collisions and responding to operator inputs.
The initial placement of obstacles and robots can be set interactively in the GUI, saved to a file and loaded from a file.
They can be removed by clicking on them with mouse.

### Team Members
- Adam Mrkva    - xmrkva04
- Daniel Jacobs - xjacob00

## Compilation and Execution Instructions
This application is built and managed using Maven. To compile and run the project, follow these steps:

### Prerequisites
Ensure that you have Maven installed and correctly setup on your system.

### Compiling the Application
1. Open a terminal or command prompt.
2. Navigate to the root directory of the project where the `pom.xml` file is located.
3. Run the following command to compile the project:

    mvn clean compile

### Generating javadocs

    mvn javadoc:javadoc

### Running the Application
After successfully compiling the project, you can run the application using the following command:

    mvn javafx:run

This will start the simulation GUI, where you can interact with the robots and the environment.