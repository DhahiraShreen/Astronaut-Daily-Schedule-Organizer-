package com.astronaut;

import com.astronaut.service.TaskFactory;
import com.astronaut.observer.TaskObserver;
import com.astronaut.service.ScheduleManager;
import com.astronaut.model.Task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ScheduleManager scheduleManager = ScheduleManager.getInstance();
        scheduleManager.addObserver(new TaskObserver());
        boolean exit = false;

        while (!exit) {
            System.out.println("Astronaut Scheduler Menu:");
            System.out.println("1. Add Task");
            System.out.println("2. View All Tasks");
            System.out.println("3. Remove Task");
            System.out.println("4. Edit Task");
            System.out.println("5. Mark Task as Completed");
            System.out.println("6. View Tasks by Priority");
            System.out.println("7. View Tasks by Category");
            System.out.println("8. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.println(addTask(scanner, scheduleManager));
                    break;
                case 2:
                    viewTasks(scheduleManager);
                    break;
                case 3:
                    System.out.println(removeTask(scanner, scheduleManager));
                    break;
                case 4:
                    System.out.println(editTask(scanner, scheduleManager));
                    break;
                case 5:
                    System.out.println(markTaskAsCompleted(scanner, scheduleManager));
                    break;
                case 6:
                    viewTasksByPriority(scanner, scheduleManager);
                    break;
                case 7:
                    viewTasksByCategory(scanner, scheduleManager);
                    break;
                case 8:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static String addTask(Scanner scanner, ScheduleManager scheduleManager) {
        String result = getTaskFromInput(scanner);
        if (result.startsWith("Error:")) {
            return result;
        }
        String[] parts = result.split("\\|");
        Task task = TaskFactory.createTask(parts[0], LocalDateTime.parse(parts[1], formatter), LocalDateTime.parse(parts[2], formatter), parts[3], parts[4]);
        return scheduleManager.addTask(task);
    }

    private static String editTask(Scanner scanner, ScheduleManager scheduleManager) {
        System.out.println("Enter the description of the task to edit:");
        String oldDescription = scanner.nextLine();

        if (!taskExists(oldDescription, scheduleManager)) {
            return "Error: No such task available.";
        }

        String result = getTaskFromInput(scanner);
        if (result.startsWith("Error:")) {
            return result;
        }
        String[] parts = result.split("\\|");
        Task newTask = TaskFactory.createTask(parts[0], LocalDateTime.parse(parts[1], formatter), LocalDateTime.parse(parts[2], formatter), parts[3], parts[4]);
        return scheduleManager.editTask(oldDescription, newTask);
    }

    private static String markTaskAsCompleted(Scanner scanner, ScheduleManager scheduleManager) {
        System.out.println("Enter the description of the task to mark as completed:");
        String description = scanner.nextLine();
        return scheduleManager.markTaskAsCompleted(description);
    }

    private static void viewTasksByPriority(Scanner scanner, ScheduleManager scheduleManager) {
        System.out.println("Enter the priority level (High, Medium, Low):");
        String priority = scanner.nextLine();
        List<Task> tasks = scheduleManager.getTasksByPriority(priority);
        if (tasks.isEmpty()) {
            System.out.println("No tasks found with priority: " + priority);
        } else {
            System.out.println("Viewing tasks with priority " + priority + ":");
            for (Task task : tasks) {
                System.out.println(formatTask(task));
            }
        }
    }

    private static void viewTasksByCategory(Scanner scanner, ScheduleManager scheduleManager) {
        System.out.println("Enter the category (Leisure, Self Care, Work):");
        String category = scanner.nextLine();
        if (!category.equalsIgnoreCase("Leisure") && !category.equalsIgnoreCase("Self Care") && !category.equalsIgnoreCase("Work")) {
            System.out.println("Error: Invalid category. Please enter Leisure, Self Care, or Work.");
            return;
        }
        List<Task> tasks = scheduleManager.getTasksByCategory(category);
        if (tasks.isEmpty()) {
            System.out.println("No tasks found in category: " + category);
        } else {
            System.out.println("Viewing tasks in category " + category + ":");
            for (Task task : tasks) {
                System.out.println(formatTask(task));
            }
        }
    }

    private static boolean taskExists(String description, ScheduleManager scheduleManager) {
        return scheduleManager.getTasks().stream()
                .anyMatch(task -> task.getDescription().equalsIgnoreCase(description));
    }

    private static String getTaskFromInput(Scanner scanner) {
        System.out.println("Enter task description:");
        String description = scanner.nextLine();

        System.out.println("Enter task start time (yyyy-MM-dd HH:mm):");
        String startTimeInput = scanner.nextLine();
        LocalDateTime startTime;
        try {
            startTime = LocalDateTime.parse(startTimeInput, formatter);
        } catch (DateTimeParseException e) {
            return "Error: Invalid time format.";
        }

        System.out.println("Enter task end time (yyyy-MM-dd HH:mm):");
        String endTimeInput = scanner.nextLine();
        LocalDateTime endTime;
        try {
            endTime = LocalDateTime.parse(endTimeInput, formatter);
        } catch (DateTimeParseException e) {
            return "Error: Invalid time format.";
        }

        LocalDateTime now = LocalDateTime.now();
        if (startTime.isBefore(now) || endTime.isBefore(now)) {
            return "Error: Task times must be in the future.";
        }

        if (endTime.isBefore(startTime)) {
            return "Error: End time cannot be before start time.";
        }

        System.out.println("Enter task priority level (High, Medium, Low):");
        String priorityLevel = scanner.nextLine();
        if (!priorityLevel.equalsIgnoreCase("High") && !priorityLevel.equalsIgnoreCase("Medium") && !priorityLevel.equalsIgnoreCase("Low")) {
            return "Error: Invalid priority level. Please enter High, Medium, or Low.";
        }

        System.out.println("Enter task category (Leisure, Self Care, Work):");
        String category = scanner.nextLine();
        if (!category.equalsIgnoreCase("Leisure") && !category.equalsIgnoreCase("Self Care") && !category.equalsIgnoreCase("Work")) {
            return "Error: Invalid category. Please enter Leisure, Self Care, or Work.";
        }

        return String.format("%s|%s|%s|%s|%s", description, startTime.format(formatter), endTime.format(formatter), priorityLevel, category);
    }

    private static void viewTasks(ScheduleManager scheduleManager) {
        List<Task> tasks = scheduleManager.getTasksSortedByStartTime();
        if (tasks.isEmpty()) {
            System.out.println("No tasks scheduled for the day.");
        } else {
            System.out.println("Viewing all tasks:");
            for (Task task : tasks) {
                System.out.println(formatTask(task));
            }
        }
    }

    private static String removeTask(Scanner scanner, ScheduleManager scheduleManager) {
        System.out.println("Enter task description to remove:");
        String description = scanner.nextLine();
        return scheduleManager.removeTask(description);
    }

    private static String formatTask(Task task) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String startTime = task.getStartTime().format(timeFormatter);
        String endTime = task.getEndTime().format(timeFormatter);
        return String.format("%s - %s: %s [%s] %s %s", startTime, endTime, task.getDescription(), task.getPriorityLevel(), task.isCompleted() ? "(Completed)" : "", task.getCategory());
    }
}
