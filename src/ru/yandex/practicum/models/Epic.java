package ru.yandex.practicum.models;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtaskIds;

    public Epic(String name, String description, int uniqueID, String status) {
        super(name, description, uniqueID, status);
    }

    public ArrayList<Integer> getSubtaskIds() {

        return subtaskIds;
    }

    public void setSubtaskIds(ArrayList<Integer> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }

    @Override
    public String toString() {
        String result = "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", uniqueID=" + uniqueID +
                ", status='" + status + '\'';
        if (subtaskIds != null) {
            result = result + ", subtaskIds=" + subtaskIds.size() + '}';
        } else {
            result = result + ", subtaskIds=null" + '}';
        }
        return result;
    }
}
