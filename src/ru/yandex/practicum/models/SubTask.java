package ru.yandex.practicum.models;

import java.util.Objects;

public class SubTask extends Task {
    protected int epicId;

    public int getEpicId() {
        return epicId;
    }

    public SubTask(String name, String description, int uniqueID, String status, int epicId) {
        super(name, description, uniqueID, status);
        this.epicId = epicId;
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
        return "SubTask{" +
                "epicId=" + epicId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", uniqueID=" + uniqueID +
                ", status='" + status + '\'' + '}';
    }
}
