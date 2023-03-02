package ru.yandex.practicum.models;

import ru.yandex.practicum.enums.Status;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtaskIds = new ArrayList<>();

    public Epic(String name, String description, int uniqueID, Status status) {
        super(name, description, uniqueID, status);
    }

    public ArrayList<Integer> getSubtaskIds() {

        return subtaskIds;
    }

    public void setSubtaskIds(ArrayList<Integer> subtaskIds) {

        this.subtaskIds = subtaskIds;
    }

    public void addSubtaskId(int id) {
        if(!subtaskIds.contains(id)) {
            subtaskIds.add(id);
        }
    }

    public void removeSubtaskId(int id) {
        subtaskIds.remove(Integer.valueOf(id));
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
