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
            bw.write(historyToString(super.getHistoryManager()));

        } catch (ManagerSaveException e) {
            throw new ManagerSaveException("Ошибка сохранения файла.");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) throws FileNotFoundException {
        final FileBackedTaskManager taskManager = new FileBackedTaskManager(file.toString());
        try (BufferedReader br = new BufferedReader(new FileReader(file.toString()))) {
            String line = null;
            while (!(line = br.readLine()).isBlank()) {

                if (!line.contains("id,type")) {
                    String[] types = line.split(",");
                    Tasks type = Tasks.valueOf(types[1]);
                    switch (type) {
                        case TASK:
                            Task task = fromString(line);
                            taskManager.tasks.put(task.getUniqueID(), task);
                            break;
                        case EPIC:
                            Epic epic = (Epic) fromString(line);
                            taskManager.epics.put(epic.getUniqueID(), epic);
                            break;
                        case SUBTASK:
                            SubTask subTask = (SubTask) fromString(line);
                            taskManager.subtasks.put(subTask.getUniqueID(), subTask);
                            break;
                    }
                }
            }
            if (br.ready()) {
                line = br.readLine();
                List<Integer> loadedHistory = historyFromString(line);
                for (Integer id : loadedHistory) {
                    if (taskManager.tasks.containsKey(id)) {
                        taskManager.getTask(id);
                    } else if (taskManager.epics.containsKey(id)) {
                        taskManager.getEpic(id);
                    } else if (taskManager.subtasks.containsKey(id)) {
                        taskManager.getSubtask(id);
                    }
                }
            }

        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return taskManager;
    }

    private static String historyToString(HistoryManager<Task> historyManager) {
        StringBuilder sb = new StringBuilder();
        for (Task task : historyManager.getHistory()) {
            sb.append(task.getUniqueID());
            sb.append(",");
        }
        return sb.toString();
    }

    private static List<Integer> historyFromString (String value) {
        List<Integer> list = new ArrayList<>();
        String[] ids = value.split(",");
        for (String i : ids) {
            list.add(Integer.parseInt(i));
        }

        return list;
    }

    private static Task fromString(String value) {
        String[] fields = value.split(",");
        int id = Integer.parseInt(fields[0]);
        Tasks type = Tasks.valueOf(fields[1]);
        String name = fields[2];
        Status status = Status.valueOf(fields[3]);
        String description = fields[4];

        switch (type) {
            case TASK:
                return new Task(name, description, id, status);
            case SUBTASK: // только у сабтаски есть значение по 5 индексу - epicId
                return new SubTask(name, description, id, status, Integer.parseInt(fields[5]));
            case EPIC:
                return new Epic(name, description, id, status);
        }

        return null;
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
        save();
    }

    @Override
    public void createSubTask(SubTask subTask) {
        super.createSubTask(subTask);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteSubTask(int id) {
        super.deleteSubTask(id);
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public Task getTask(int id) { // переопределяем эти методы, чтобы сохранять историю, когда обращаемся к задачам
        super.getTask(id);
        save();
        return super.getTask(id);
    }

    @Override
    public SubTask getSubtask(int id) {
        super.getSubtask(id);
        save();
        return super.getSubtask(id);
    }

    @Override
    public Epic getEpic(int id) {
        super.getEpic(id);
        save();
        return super.getEpic(id);
    }


    public static void main(String[] args) throws FileNotFoundException {
        // 1 сценарий:
        /*FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager("resources/save.csv");
        fileBackedTaskManager.createTask(new Task("sample", "just for fun", 0, Status.NEW));
        fileBackedTaskManager.createEpic(new Epic("sample2", "just for fun2", 0, Status.NEW));
        fileBackedTaskManager.createSubTask(new SubTask("sample3", "just for fun3", 0,
                Status.NEW, 2));
        fileBackedTaskManager.updateSubTask(new SubTask("sample3", "just for fun3", 3,
                Status.DONE, 2));

        fileBackedTaskManager.getTask(1);*/

        // 2 сценарий:
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(new
                File("resources/save.csv"));


        System.out.println(fileBackedTaskManager.getTasks());
        System.out.println(fileBackedTaskManager.getEpics());
        System.out.println(fileBackedTaskManager.getSubtasks());

        fileBackedTaskManager.getHistory();

    }
}
