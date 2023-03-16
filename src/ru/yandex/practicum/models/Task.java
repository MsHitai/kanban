package ru.yandex.practicum.models;

import ru.yandex.practicum.enums.Status;
import ru.yandex.practicum.enums.Tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.yandex.practicum.enums.Tasks.TASK;

public class Task {
    protected String name;
    protected String description;
    protected int uniqueID;
    protected Status status;

    protected Tasks type;

    protected int duration; // число в минутах
    LocalDateTime startTime;

    public Task(String name, String description, int uniqueID, Status status, int duration,
                LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.uniqueID = uniqueID;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
        this.type = TASK;
    }

    public Task(String name, String description, int uniqueID, Status status) {
        this.name = name;
        this.description = description;
        this.uniqueID = uniqueID;
        this.status = status;
        this.type = TASK;
    }

    public LocalDateTime getEndTime() {
        startTime = startTime.plusMinutes(duration);
        return startTime;
    }

    public int getUniqueID() {
        return uniqueID;
    }

    public Status getStatus() {
        return status;
    }

    public void setUniqueID(int uniqueID) {

        this.uniqueID = uniqueID;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy HH:mm");
        String formattedTaskDate = startTime.format(formatter);
        return uniqueID + "," + type + "," +
                name + "," + status + "," + description + "," + formattedTaskDate + "," + duration + "\n";
    }
}
