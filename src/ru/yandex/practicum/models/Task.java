package ru.yandex.practicum.models;

import ru.yandex.practicum.enums.Status;
import ru.yandex.practicum.enums.Tasks;

import static ru.yandex.practicum.enums.Tasks.TASK;

public class Task {
    protected String name;
    protected String description;
    protected int uniqueID;
    protected Status status;

    protected Tasks type;

    public Task(String name, String description, int uniqueID, Status status) {
        this.name = name;
        this.description = description;
        this.uniqueID = uniqueID;
        this.status = status;
        this.type = TASK;
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
        return uniqueID + "," + type + "," +
                name + "," + status + "," + description + "," +  "\n";
    }
}
