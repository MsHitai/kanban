package ru.yandex.practicum.service;

import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    int createID();

    void createTask (Task task);
    void createSubTask(SubTask subTask);
    void createEpic(Epic epic);
    Task getTask(int id);
    SubTask getSubtask(int id);
    Epic getEpic(int id);
    List<Task> getTasks();
    public List<SubTask> getSubtasks();
    public List<Epic> getEpics();
    void updateTask(Task task);
    void updateSubTask(SubTask subTask);
    void updateEpic(Epic epic);
    ArrayList<SubTask> getSubTasksByEpics(Epic epic);
    void deleteTask(int id);
    void deleteSubTask(int id);
    void deleteEpic(int id);
    void deleteAllTasks();
    void deleteAllSubTasks();
    void deleteAllEpics();
}
