package ru.yandex.practicum.models;

public class Task {
    protected String name;
    protected String description;
    protected int uniqueID;
    protected String status;

    public Task(String name, String description, int uniqueID, String status) {
        this.name = name;
        this.description = description;
        this.uniqueID = uniqueID;
        this.status = status;

    }

    public int getUniqueID() {
        return uniqueID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", uniqueID=" + uniqueID +
                ", status='" + status + '\'' + '}';
    }
}
