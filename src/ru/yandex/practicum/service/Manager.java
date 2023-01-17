package ru.yandex.practicum.service;

import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.Task;

import java.util.ArrayList;
import java.util.HashMap;


public class Manager {
    int uniqueID;

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public int createID() {

        return ++uniqueID;
    }

    public void createTask(Task task) {
        tasks.put(task.getUniqueID(), task);
    }

    public void createSubTask(SubTask subTask) {
        subTasks.put(subTask.getUniqueID(), subTask);
    }

    public void createEpic(Epic epic) {
        epics.put(epic.getUniqueID(), epic);
        updateEpicStatus(epic);
    }

    public void refreshTask(String newName, String newDescription, int taskID, String status) {
        Task task = new Task(newName, newDescription, taskID, status);
        tasks.put(taskID, task);
    }

    public Task getTask(int id) {
        Task task = null;
        if (tasks != null) {
            if (tasks.containsKey(id)) {
                task = tasks.get(id);
            }
        }
        return task;
    }

    public SubTask getSubtask(int id) {
        SubTask subTask = null;
        if (subTasks != null) {
            if (subTasks.containsKey(id)) {
                subTask = subTasks.get(id);
            }
        }
        return subTask;
    }

    public Epic getEpic(int id) {
        Epic epic = null;
        if (epics != null) {
            if (epics.containsKey(id)) {
                epic = epics.get(id);
            }
        }
        return epic;
    }

    public void deleteTask(int id) {
        if (!tasks.isEmpty()) {
            if (tasks.containsKey(id)) {
                tasks.remove(id);
            }
        }
    }

    public void deleteAllTasks() {
        if (!tasks.isEmpty()) {
            tasks.clear();
        }
    }

    public void refreshSubTask(String newName, String newDescription, int subTaskID, String status, int epicId) {
        SubTask subTask = new SubTask(newName, newDescription, subTaskID, status, epicId);
        subTasks.put(subTaskID, subTask);
        if (epics.containsKey(epicId)) {
            sortSubTasksByEpics(getEpic(epicId), epicId);
            updateEpicStatus(getEpic(epicId));
        }
    }

    public void deleteAllSubTasks() {
        if (subTasks != null) {
            subTasks.clear();
            if (epics != null) {
                for (Epic epic : epics.values()) {
                    updateEpicStatus(epic);
                }
            }
        }
    }

    public void deleteSubTask(int id) {
        if (!subTasks.isEmpty()) {
            if (subTasks.containsKey(id)) {
                Epic epic = getEpic(subTasks.get(id).getEpicId());
                subTasks.remove(id);
                updateEpicStatus(epic);
            }
        }
    }

    private void updateEpicStatus(Epic epic) {
        sortSubTasksByEpics(epic, epic.getUniqueID()); // обновляем список подзадач у эпика
        if ((epic.getSubtaskIds() == null) || epic.getSubtaskIds().isEmpty()) {
            epic.setStatus("NEW");
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
                if (subTask.getStatus().equals("NEW")) {
                    newSum--;
                } else if (subTask.getStatus().equals("DONE")) {
                    doneSum--;
                }
            }
            if (newSum == 0) {
                epic.setStatus("NEW");
            } else if (doneSum == 0) {
                epic.setStatus("DONE");
            } else {
                epic.setStatus("IN_PROGRESS");
            }
        }
    }

    public void refreshEpic(String newName, String newDescription, int epicID, String status) {
        Epic epic = new Epic(newName, newDescription, epicID, status);
        sortSubTasksByEpics(epic, epicID);
        updateEpicStatus(getEpic(epicID));
        epics.put(epicID, getEpic(epicID));
    }

    public void deleteEpic(int id) {
        if (epics != null) {
            if (epics.containsKey(id)) {
                if (getEpic(id).getSubtaskIds() != null) {
                    for (int i = 0; i < getEpic(id).getSubtaskIds().size(); i++) {
                        if (subTasks != null) {
                            deleteSubTask((getEpic(id).getSubtaskIds().get(i)));
                        }
                    }
                    getEpic(id).getSubtaskIds().clear();
                    epics.remove(id);
                }
            }
        }
    }

    public void deleteAllEpics() {
        if (epics != null) {
            epics.clear();
        }
        deleteAllSubTasks();
    }

    public void sortSubTasksByEpics(Epic epic, int epicID) {
        ArrayList<Integer> subtasks = new ArrayList<>();
        for (SubTask subTask : subTasks.values()) {
            if (subTask.getEpicId() == epicID) {
                subtasks.add(subTask.getUniqueID());
            }
        }
        if (!subtasks.isEmpty()) {
            epic.fillMySubTasks(subtasks);
        }
    }

    public ArrayList<SubTask> getSubTasksByEpics(Epic epic) {
        ArrayList<SubTask> subTasks1 = new ArrayList<>();
        if (epic.getSubtaskIds() != null) {
            if (!epic.getSubtaskIds().isEmpty()) {
                for (int i = 0; i < epic.getSubtaskIds().size(); i++) {
                    subTasks1.add(getSubtask(epic.getSubtaskIds().get(i)));
                }
            }
        }
        return subTasks1;
    }

}
