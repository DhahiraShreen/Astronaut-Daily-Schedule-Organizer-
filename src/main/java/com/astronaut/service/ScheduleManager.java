package com.astronaut.service;

import com.astronaut.model.Task;
import com.astronaut.observer.Observer;
import com.astronaut.utils.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ScheduleManager {
    private static ScheduleManager instance;
    private List<Task> tasks;
    private Logger logger;

    private ScheduleManager() {
        tasks = new ArrayList<>();
        logger = Logger.getInstance();
    }

    public static ScheduleManager getInstance() {
        if (instance == null) {
            instance = new ScheduleManager();
        }
        return instance;
    }

    public String addTask(Task task) {
        for (Task t : tasks) {
            if (t.conflictsWith(task)) {
                String message = "Error: Task conflicts with existing task \"" + t.getDescription() + "\".";
                notifyObservers(message);
                logger.log(message, task.getDescription());
                return message;
            }
        }
        tasks.add(task);
        String message = "Task added successfully. No conflicts.";
        notifyObservers(message);
        logger.log(message, task.getDescription());
        return message;
    }

    public String removeTask(String description) {
        Optional<Task> taskToRemove = tasks.stream()
                                           .filter(task -> task.getDescription().equalsIgnoreCase(description))
                                           .findFirst();
        if (taskToRemove.isPresent()) {
            tasks.remove(taskToRemove.get());
            String message = "Task removed successfully.";
            notifyObservers(message);
            logger.log(message, description);
            return message;
        } else {
            String message = "Error: No such task available.";
            notifyObservers(message);
            logger.log(message, description);
            return message;
        }
    }

    public String editTask(String oldDescription, Task newTask) {
        Optional<Task> taskToEdit = tasks.stream()
                                         .filter(task -> task.getDescription().equalsIgnoreCase(oldDescription))
                                         .findFirst();
        if (taskToEdit.isPresent()) {
            tasks.remove(taskToEdit.get());
            String addTaskResult = addTask(newTask);
            if (addTaskResult.contains("successfully")) {
                String message = "Task edited successfully.";
                notifyObservers(message);
                logger.log(message, newTask.getDescription());
                return message;
            } else {
                return addTaskResult;
            }
        } else {
            String message = "Error: No such task available.";
            notifyObservers(message);
            logger.log(message, oldDescription);
            return message;
        }
    }

    public String markTaskAsCompleted(String description) {
        Optional<Task> taskToComplete = tasks.stream()
                                             .filter(task -> task.getDescription().equalsIgnoreCase(description))
                                             .findFirst();
        if (taskToComplete.isPresent()) {
            taskToComplete.get().markAsCompleted();
            String message = "Task marked as completed.";
            notifyObservers(message);
            logger.log(message, description);
            return message;
        } else {
            String message = "Error: No such task available.";
            notifyObservers(message);
            logger.log(message, description);
            return message;
        }
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public List<Task> getTasksSortedByStartTime() {
        List<Task> sortedTasks = new ArrayList<>(tasks);
        sortedTasks.sort(Comparator.comparing(Task::getStartTime));
        return sortedTasks;
    }

    public List<Task> getTasksByPriority(String priority) {
        return tasks.stream()
                    .filter(task -> task.getPriorityLevel().equalsIgnoreCase(priority))
                    .collect(Collectors.toList());
    }

    private List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    private void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }
}
