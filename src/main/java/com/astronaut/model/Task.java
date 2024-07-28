package com.astronaut.model;

import java.time.LocalDateTime;

public class Task {
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String priorityLevel;
    private boolean completed;

    public Task(String description, LocalDateTime startTime, LocalDateTime endTime, String priorityLevel) {
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.priorityLevel = priorityLevel;
        this.completed = false;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getPriorityLevel() {
        return priorityLevel;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void markAsCompleted() {
        this.completed = true;
    }

    public boolean conflictsWith(Task other) {
        return !startTime.isAfter(other.endTime) && !endTime.isBefore(other.startTime);
    }

    @Override
    public String toString() {
        return String.format("%s - %s: %s [%s] %s", startTime.toLocalTime(), endTime.toLocalTime(), description, priorityLevel, completed ? "(Completed)" : "");
    }
}
