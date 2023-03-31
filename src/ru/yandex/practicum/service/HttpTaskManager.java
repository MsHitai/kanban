package ru.yandex.practicum.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class HttpTaskManager extends FileBackedTaskManager implements TaskManager {

    private final KVTaskClient client;

    public HttpTaskManager(String path) throws URISyntaxException, IOException, InterruptedException {
        super(path);
        URI uri = new URI(path);
        client = new KVTaskClient(uri);
        client.register();
    }

    @Override
    protected void save() {
        String value = separateTasksFromLists(super.getTasks()) +
                separateTasksFromLists(super.getEpics()) +
                separateTasksFromLists(super.getSubtasks()) +
                "\n" +
                historyToString(super.getHistoryManager());
        try {
            client.put("HttpTaskManager", value);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String load() throws IOException, InterruptedException {
        return client.load("HttpTaskManager");
    }

    public KVTaskClient getClient() {
        return client;
    }
}