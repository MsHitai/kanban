package ru.yandex.practicum.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.Task;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class HttpTaskManager extends FileBackedTaskManager implements TaskManager {

    private final KVTaskClient client;
    private final Gson gson = new Gson();

    public HttpTaskManager(String path) throws URISyntaxException {
        super(null);
        URI uri = new URI(path);
        client = new KVTaskClient(uri);
        load();
    }

    @Override
    protected void save() {
        String jsonTasks = gson.toJson(new ArrayList<>(tasks.values()));
        client.put("tasks", jsonTasks);

        String jsonEpics = gson.toJson(new ArrayList<>(epics.values()));
        client.put("epics", jsonEpics);

        String jsonSubtasks = gson.toJson(new ArrayList<>(subtasks.values()));
        client.put("subtasks", jsonSubtasks);

        String jsonHistory = gson.toJson(new ArrayList<>(getHistory()));
        client.put("history", jsonHistory);
    }

    private void load() {
        loadTasks();
        loadEpics();
        loadSubtasks();
        loadHistory();
    }

    private void loadTasks() {
        ArrayList<Task> tasks = gson.fromJson(client.load("tasks"), new TypeToken<ArrayList<Task>>() {
        }.getType());

        if (tasks == null) {
            return;
        }

        for (Task task : tasks) {
            final int id = task.getUniqueID();
            if (id > this.uniqueId) {
                this.uniqueId = id;
            }
            this.tasks.put(id, task);
            if (checkTimeClashes(task)) {
                prioritizedTasks.add(task);
            }
        }
    }

    private void loadEpics() {
        ArrayList<Epic> epics = gson.fromJson(client.load("epics"), new TypeToken<ArrayList<Epic>>() {
        }.getType());

        if (epics == null) {
            return;
        }

        for (Epic epic : epics) {
            final int id = epic.getUniqueID();
            if (id > this.uniqueId) {
                this.uniqueId = id;
            }
            this.epics.put(id, epic);
        }
    }

    private void loadSubtasks() {
        ArrayList<SubTask> subtasks = gson.fromJson(client.load("subtasks"), new TypeToken<ArrayList<SubTask>>() {
        }.getType());

        if (subtasks == null) {
            return;
        }

        for (SubTask subTask : subtasks) {
            final int id = subTask.getUniqueID();
            if (id > this.uniqueId) {
                this.uniqueId = id;
            }
            if (checkTimeClashes(subTask)) {
                this.subtasks.put(id, subTask);
            }
        }
    }

    private void loadHistory() {
        ArrayList<Task> history = gson.fromJson(client.load("history"), new TypeToken<ArrayList<Task>>() {
        }.getType());

        if (history == null) {
            return;
        }

        for (Task task : history) {
            int id = task.getUniqueID();

            if (this.tasks.containsKey(id)) {
                getTask(id);
            } else if (this.epics.containsKey(id)) {
                getEpic(id);
            } else if (this.subtasks.containsKey(id)) {
                getSubtask(id);
            }
        }
    }
}