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
    private static List<String> tasksToLoadFrom = new ArrayList<>();

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

    public static FileBackedTaskManager loadFromFile(File file) throws FileNotFoundException {

        try (BufferedReader br = new BufferedReader(new FileReader(file.toString()))) {
            while (br.ready()) {
                String line = br.readLine();
                tasksToLoadFrom.add(line);
            }
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int index = tasksToLoadFrom.size() - 1;
        InMemoryHistoryManager.historyFromString(tasksToLoadFrom.get(index));

        return new FileBackedTaskManager(file.toString());
    }

    private void loadTasks(List<String> tasks) { // можно разбить загрузку на 2 действия? не могу сделать этот метод статическим
        // чтобы можно было вызвать в статическом методе, т.к. переменные у тасок не статические, или есть какой-то вариант?
        for (int i = 1; i < tasks.size() - 1; i++) {
            String[] types = tasks.get(i).split(","); // делим каждую строку на массивы из слов
            if (!tasks.get(i).isBlank() || !tasks.get(i).isEmpty()) {
            Tasks type = Tasks.valueOf(types[1]);
            switch (type) {
                case TASK:
                    super.createTask(fromString(tasks.get(i)));
                    break;
                case EPIC:
                    super.createEpic((Epic) fromString(tasks.get(i)));
                    break;
                case SUBTASK:
                    super.createSubTask((SubTask) fromString(tasks.get(i)));
                    break;
            }
        }
    }

}

    private Task fromString(String value) {
        int id;
        Tasks type;
        String name;
        Status status;
        String description;

        String[] fields = value.split(",");
        id = Integer.parseInt(fields[0]);
        type = Tasks.valueOf(fields[1]);
        name = fields[2];
        status = Status.valueOf(fields[3]);
        description = fields[4];

        switch (type) {
            case TASK:
                return new Task(name, description, id, status);
            case SUBTASK:
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
    public Task getTask(int id) { // переопределяем эти методы, чтобы сохранять историю, когда обращаемся к задачам
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
    public Epic getEpic(int id) { // вызов этого метода в createSubtask обновляет историю из файла
        super.getEpic(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
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
        fileBackedTaskManager.loadTasks(tasksToLoadFrom);

        System.out.println(fileBackedTaskManager.getTasks());
        System.out.println(fileBackedTaskManager.getEpics());
        System.out.println(fileBackedTaskManager.getSubtasks());

        fileBackedTaskManager.getHistory();

    }
}
