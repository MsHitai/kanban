package ru.yandex.practicum.models;

import ru.yandex.practicum.enums.Status;
import ru.yandex.practicum.enums.Tasks;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubTask subTask = (SubTask) o;
        return epicId == subTask.epicId
                && uniqueID == subTask.uniqueID
                && name.equals(subTask.name)
                && description.equals(subTask.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(epicId, name, description, uniqueID);
    }

    @Override
    public String toString() {
        return uniqueID + "," + type + "," +
                name + "," + status + "," + description + "," + epicId + "\n";
    }
}
