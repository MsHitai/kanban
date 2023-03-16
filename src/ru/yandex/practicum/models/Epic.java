package ru.yandex.practicum.models;

import ru.yandex.practicum.enums.Status;
import ru.yandex.practicum.enums.Tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> subtaskIds = new ArrayList<>();

    private LocalDateTime endTime;

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration (int duration) {
        this.duration = duration;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return subtaskIds.equals(epic.subtaskIds) && endTime.equals(epic.endTime)
                && uniqueID == epic.uniqueID && duration == epic.duration
                && Objects.equals(name, epic.name)
                && Objects.equals(description, epic.description)
                && status == epic.status && type == epic.type
                && startTime.equals(epic.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskIds, endTime);
    }

    @Override
    public String toString() {
        return uniqueID + "," + type + "," +
                name + "," + status + "," + description + "," +  "\n";
    }
}
