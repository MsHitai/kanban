package ru.yandex.practicum.service;

import ru.yandex.practicum.models.Task;

import java.io.IOException;
import java.net.URISyntaxException;

public class Managers {

    public static TaskManager getDefault() throws URISyntaxException, IOException, InterruptedException {
        return new HttpTaskManager("http://localhost:8078/");
    }

    public static TaskManager getDefaultFile() {
        return new FileBackedTaskManager("resources/save.csv");
    }

    public static HistoryManager<Task> getDefaultHistory() {
        return new InMemoryHistoryManager<>();
    }
}
