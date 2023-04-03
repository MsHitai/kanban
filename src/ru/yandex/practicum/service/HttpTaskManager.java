package ru.yandex.practicum.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.Task;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager extends FileBackedTaskManager implements TaskManager {

    private final KVTaskClient client;
    private final Gson gson = new Gson();

    public HttpTaskManager(String path) throws URISyntaxException {
        this(path, false);
    }

    public HttpTaskManager(String path, boolean load) throws URISyntaxException {
        super(null);
        URI uri = new URI(path);
        client = new KVTaskClient(uri);
        if (load) {
            load();
        }
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
        ArrayList<Task> tasks = gson.fromJson(client.load("tasks"), new TypeToken<ArrayList<Task>>() {
        }.getType());
        addTasks(tasks);

        ArrayList<Epic> epics = gson.fromJson(client.load("epics"), new TypeToken<ArrayList<Epic>>() {
        }.getType());
        addTasks(epics);

        ArrayList<SubTask> subtasks = gson.fromJson(client.load("subtasks"), new TypeToken<ArrayList<SubTask>>() {
        }.getType());
        addTasks(subtasks);

        loadHistory();
    }

    protected void addTasks(List<? extends Task> tasks) {
        if (tasks == null) {
            return;
        }

        for (Task task : tasks) {
            final int id = task.getUniqueID();
            if (id > this.uniqueId) {
                this.uniqueId = id;
            }
            if (task instanceof Epic) {
                this.epics.put(id, (Epic) task);
            } else if (task instanceof SubTask) {
                this.subtasks.put(id, (SubTask) task);
                if (checkTimeClashes(task)) {
                    prioritizedTasks.add(task);
                }
            } else {
                this.tasks.put(id, task);
                if (checkTimeClashes(task)) {
                    prioritizedTasks.add(task);
                }
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
                getHistoryManager().add(tasks.get(id));
            } else if (this.epics.containsKey(id)) {
                getHistoryManager().add(epics.get(id));
            } else if (this.subtasks.containsKey(id)) {
                getHistoryManager().add(subtasks.get(id));
            }
        }
    }
}