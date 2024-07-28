package com.astronaut.service;

import com.astronaut.model.Task;

import java.time.LocalDateTime;

public class TaskFactory {
    public static Task createTask(String description, LocalDateTime startTime, LocalDateTime endTime, String priorityLevel) {
        return new Task(description, startTime, endTime, priorityLevel);
    }
}
