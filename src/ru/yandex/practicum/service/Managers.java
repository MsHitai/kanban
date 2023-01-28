package ru.yandex.practicum.service;

import ru.yandex.practicum.models.Task;

public class Managers {

    public static InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
    public static InMemoryHistoryManager<Task> inMemoryHistoryManager = new InMemoryHistoryManager<>();

    public static TaskManager getDefault() {
        return inMemoryTaskManager;
    }

    public static HistoryManager<Task> getDefaultHistory() {
        return inMemoryHistoryManager;
    }
}
