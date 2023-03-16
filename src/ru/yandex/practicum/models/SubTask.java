package ru.yandex.practicum.models;

import ru.yandex.practicum.enums.Status;
import ru.yandex.practicum.enums.Tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class SubTask extends Task {
    protected int epicId;

    public int getEpicId() {
        return epicId;
    }

    public SubTask(String name, String description, int uniqueID, Status status, int epicId) {
        super(name, description, uniqueID, status);
        this.epicId = epicId;
        this.type = Tasks.SUBTASK;
    }

    public SubTask(String name, String description, int uniqueID, Status status, int duration, LocalDateTime startTime,
                   int epicId) {
        super(name, description, uniqueID, status, duration, startTime);
        this.epicId = epicId;
        this.type = Tasks.SUBTASK;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubTask subTask = (SubTask) o;
        return uniqueID == subTask.uniqueID && duration == subTask.duration
                && Objects.equals(name, subTask.name)
                && Objects.equals(description, subTask.description)
                && status == subTask.status && type == subTask.type
                && startTime.equals(subTask.startTime)
                && epicId == subTask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(epicId, name, description, uniqueID, status, type, duration, startTime);
    }



    @Override
    public String toString() {
        String formattedTaskDate;
        if (startTime == null) { // чтобы не делать parse от null
            formattedTaskDate = null;
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy HH:mm");
            formattedTaskDate = startTime.format(formatter);
        }
        return uniqueID + "," + type + "," +
                name + "," + status + "," + description + "," + formattedTaskDate + "," + duration + "," +
                epicId + "," + "\n";
    }
}
