package ru.yandex.practicum.service;

import ru.yandex.practicum.enums.Status;
import ru.yandex.practicum.exceptions.ManagerSaveException;
import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.Task;

import java.io.*;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {

    private final String path;

    public FileBackedTaskManager(String path) {
        super();
        this.path = path;
    }

    private void save() throws ManagerSaveException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(this.path, false))) {
            bw.write("id,type,name,status,description,epic \n");
            bw.write(separateTasksFromLists(super.getTasks()));
            bw.write(separateTasksFromLists(super.getEpics()));
            bw.write(separateTasksFromLists(super.getSubtasks()));
            bw.write("\n");
            bw.write(InMemoryHistoryManager.historyToString(super.getHistoryManager()));

        } catch (ManagerSaveException e) {
            throw new ManagerSaveException("Ошибка сохранения файла.");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }



    private String separateTasksFromLists(List<?> list) { //достаем задачи из списка по строкам, чтобы не было [] в save
        StringBuilder sb = new StringBuilder();
        for (Object task : list) {
            sb.append(task.toString());
        }
        return sb.toString();
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

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteSubTask(int id) {
        super.deleteSubTask(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Task getTask(int id) {
        super.getTask(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
        return super.getTask(id);
    }

    @Override
    public SubTask getSubtask(int id) {
        super.getSubtask(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
        return super.getSubtask(id);
    }

    @Override
    public Epic getEpic(int id) {
        super.getEpic(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
        return super.getEpic(id);
    }


    public static void main(String[] args) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager("resources/save.csv");
        fileBackedTaskManager.createEpic(new Epic("sample2", "just for fun2", 0, Status.NEW));
        fileBackedTaskManager.createSubTask(new SubTask("sample3", "just for fun3", 0,
                Status.NEW, 1));
        fileBackedTaskManager.createTask(new Task("sample", "just for fun", 0, Status.NEW));

        fileBackedTaskManager.updateSubTask(new SubTask("sample3", "just for fun3", 2,
                Status.DONE, 1));

        fileBackedTaskManager.getTask(3);


        //fileBackedTaskManager.deleteSubTask(2);

    }
}
