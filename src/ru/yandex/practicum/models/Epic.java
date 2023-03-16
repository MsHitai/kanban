package ru.yandex.practicum.models;

import ru.yandex.practicum.enums.Status;
import ru.yandex.practicum.enums.Tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtaskIds = new ArrayList<>();

    private LocalDateTime endTime;

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Epic(String name, String description, int uniqueID, Status status) {
        super(name, description, uniqueID, status);
        this.type = Tasks.EPIC;
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
        return uniqueID + "," + type + "," +
                name + "," + status + "," + description + "," +  "\n";
    }
}
