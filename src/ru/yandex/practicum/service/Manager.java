package ru.yandex.practicum.service;

import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.Status;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Manager {
    int uniqueID;

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, SubTask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();

    public List<Task> getTasks() {
        return new ArrayList<>(this.tasks.values());
    }

    public List<SubTask> getSubtasks() {
        return new ArrayList<>(this.subtasks.values());
    }

    public List<Epic> getEpics() {
        return new ArrayList<>(this.epics.values());
    }

    public int createID() {
        return ++uniqueID;
    }

    public void createTask(Task task) {
        tasks.put(task.getUniqueID(), task);
    }

    public void createSubTask(SubTask subTask) {
        subtasks.put(subTask.getUniqueID(), subTask);
        Epic epic = getEpic(subTask.getEpicId());
        epic.addSubtaskId(subTask.getUniqueID());
        updateEpicStatus(epic);
    }

    public void createEpic(Epic epic) {
        epics.put(epic.getUniqueID(), epic);
    }

    public void updateTask(Task task) {
        int id = task.getUniqueID();
        if (tasks.containsKey(id)) {
            tasks.put(id, task);
        }
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public SubTask getSubtask(int id) {
        return subtasks.get(id);
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    public void deleteTask(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        }
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void updateSubTask(SubTask subTask) {
        int id = subTask.getUniqueID();
        int epicId = subTask.getEpicId();
        if (subtasks.containsKey(id) && epics.containsKey(epicId)) {
            subtasks.put(id, subTask);
            updateEpicStatus(getEpic(epicId));
        }
    }

    public void deleteAllSubTasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtaskIds().clear();
            updateEpicStatus(epic);
        }

    }

    public void deleteSubTask(int id) {
        if (subtasks.containsKey(id)) {
            SubTask subTask = subtasks.remove(id);
            if (subTask == null) {
                return;
            }
            Epic epic = epics.get(subTask.getEpicId());
            epic.removeSubtaskId(id);
            updateEpicStatus(epic);
        }
    }

    private void updateEpicStatus(Epic epic) {
        if ((epic.getSubtaskIds() == null) || epic.getSubtaskIds().isEmpty()) {
            epic.setStatus(Status.NEW);
        } else {
            checkSubtaskStatus(epic);
        }
    }

    private void checkSubtaskStatus(Epic epic) {
        if (!getSubTasksByEpics(epic).isEmpty()) {
            int newSum = getSubTasksByEpics(epic).size();
            int doneSum = getSubTasksByEpics(epic).size();
            for (SubTask subTask : getSubTasksByEpics(epic)) {
                if (subTask == null) {
                    continue;
                }
                if (subTask.getStatus().equals(Status.NEW)) {
                    newSum--;
                } else if (subTask.getStatus().equals(Status.DONE)) {
                    doneSum--;
                }
            }
            if (newSum == 0) {
                epic.setStatus(Status.NEW);
            } else if (doneSum == 0) {
                epic.setStatus(Status.DONE);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
    }

    public void updateEpic(Epic epic) {
        int epicID = epic.getUniqueID();
        if (epics.containsKey(epicID)) {
            epic.setSubtaskIds(getEpic(epic.getUniqueID()).getSubtaskIds());
            epics.put(epicID, epic);
            updateEpicStatus(getEpic(epicID));
        }
    }

    public void deleteEpic(int id) {
        if (epics.containsKey(id)) {
            ArrayList<Integer> tasksId = getEpic(id).getSubtaskIds();
            if (tasks != null) {
                for (Integer subtaskId : tasksId) {
                    subtasks.remove(subtaskId);
                }
                epics.remove(id);
            }
        }
    }

    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public ArrayList<SubTask> getSubTasksByEpics(Epic epic) {
        ArrayList<SubTask> tasks = new ArrayList<>();
        for (int id : epic.getSubtaskIds()) {
            tasks.add(subtasks.get(id));
        }
        return tasks;
    }

}
