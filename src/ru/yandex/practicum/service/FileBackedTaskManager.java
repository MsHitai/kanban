package ru.yandex.practicum.service;

import ru.yandex.practicum.enums.Status;
import ru.yandex.practicum.enums.Tasks;
import ru.yandex.practicum.exceptions.ManagerSaveException;
import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {

    private final String path;
    private final List<Tasks> tasksToSave = new ArrayList<>();


    public FileBackedTaskManager(String path) {
        super();
        this.path = path;
    }

    private void save() throws ManagerSaveException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(this.path, false))) {
            bw.write("id,type,name,status,description,epic \n");
            bw.write(super.getTasks().toString());
            bw.write(super.getEpics().toString());
            bw.write(super.getSubtasks().toString());
        } catch (ManagerSaveException e) {
            throw new ManagerSaveException("Ошибка сохранения файла.");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createSubTask(SubTask subTask) {
        super.createSubTask(subTask);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager("resources/save.csv");
        fileBackedTaskManager.createEpic(new Epic("sample2", "just for fun2", 0, Status.NEW));
        fileBackedTaskManager.createSubTask(new SubTask("sample3", "just for fun3", 0, Status.NEW, 1));
        fileBackedTaskManager.createTask(new Task("sample", "just for fun", 0, Status.NEW));

    }
}
