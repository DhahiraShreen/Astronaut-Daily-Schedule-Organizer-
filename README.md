# Astronaut Daily Schedule Organizer 

## Overview

The Astronaut Scheduler is a console-based application designed to help astronauts organize their daily schedules efficiently. This application allows users to manage their tasks by adding, removing, editing, and viewing them. Users can also mark tasks as completed and filter tasks based on their priority level.

## Table of Contents

- [Project Structure](#project-structure)
- [Features](#features)
- [Design Patterns](#design-patterns)
- [How to Run](#how-to-run)
- [Possible Inputs and Corresponding Outputs](#possible-inputs-and-corresponding-outputs)
- [Non-functional Requirements](#non-functional-requirements)
- [License](#license)

## Project Structure

```AstronautScheduler/
├── src/
│ ├── main/
│ │ ├── java/
│ │ │ └── com/
│ │ │ └── astronaut/
│ │ │ ├── model/
│ │ │ │ └── Task.java
│ │ │ ├── observer/
│ │ │ │ ├── Observer.java
│ │ │ │ └── TaskObserver.java
│ │ │ ├── service/
│ │ │ │ ├── ScheduleManager.java
│ │ │ │ └── TaskFactory.java
│ │ │ ├── utils/
│ │ │ │ └── Logger.java
│ │ │ └── Main.java
│ └── resources/
├── target/
├── .gitignore
├── application.log
├── dependency-reduced-pom.xml
├── pom.xml
└── README.md```


## Features

- **Add a new task**: Users can add tasks with a description, start time, end time, and priority level.
- **Remove an existing task**: Users can remove tasks by specifying the task description.
- **View all tasks sorted by start time**: Users can view all scheduled tasks sorted by their start time.
- **Edit an existing task**: Users can edit task details.
- **Mark tasks as completed**: Users can mark tasks as completed.
- **View tasks for a specific priority level**: Users can filter and view tasks based on their priority level.
- **Validate that new tasks do not overlap with existing tasks**: The application ensures no overlapping tasks.
- **Provide appropriate error messages for invalid operations**: The application provides error messages for invalid inputs and operations.

## Design Patterns

1. **Singleton Pattern**: Ensures only one instance of the ScheduleManager class manages all tasks.
2. **Factory Pattern**: Used to create Task objects.
3. **Observer Pattern**: Notifies users if a new task conflicts with an existing one.

## How to Run

1. **Clone the repository**:
    ```bash
    git clone https://github.com/your-username/AstronautScheduler.git
    cd AstronautScheduler
    ```

2. **Build the project**:
    ```bash
    mvn clean install
    ```

3. **Run the application**:
    ```bash
    mvn exec:java -Dexec.mainClass="com.astronaut.Main"
    ```

## Possible Inputs and Corresponding Outputs

### Positive Cases

1. **Add Task**:
   - **Input**: `Add Task("Morning Exercise", "07:00", "08:00", "High")`
   - **Output**: `Task added successfully. No conflicts.`

2. **View Tasks**:
   - **Input**: `View Tasks`
   - **Output**:
     ```
     07:00 - 08:00: Morning Exercise [High]
     09:00 - 10:00: Team Meeting [Medium]
     ```

3. **Remove Task**:
   - **Input**: `Remove Task("Morning Exercise")`
   - **Output**: `Task removed successfully.`

### Negative Cases

1. **Add Task with Conflict**:
   - **Input**: `Add Task("Training Session", "09:30", "10:30", "High")`
   - **Output**: `Error: Task conflicts with existing task "Team Meeting".`

2. **Remove Non-existent Task**:
   - **Input**: `Remove Task("Non-existent Task")`
   - **Output**: `Error: Task not found.`

3. **Add Task with Invalid Time**:
   - **Input**: `Add Task("Invalid Time Task", "25:00", "26:00", "Low")`
   - **Output**: `Error: Invalid time format.`

4. **View Tasks when None Exist**:
   - **Input**: `View Tasks`
   - **Output**: `No tasks scheduled for the day.`

## Non-functional Requirements

- **Graceful exception handling**: The application handles exceptions gracefully.
- **Performance optimization**: The application is optimized for performance.
- **Logging mechanism**: A logging mechanism is implemented to track application usage and errors.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
