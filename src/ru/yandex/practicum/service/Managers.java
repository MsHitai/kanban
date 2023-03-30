package ru.yandex.practicum.service;

import ru.yandex.practicum.models.Task;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager(); // todo return FileManager
    }

    public static TaskManager getDefaultFile() {
        return new FileBackedTaskManager("resources/save.csv");
    }

    public static HistoryManager<Task> getDefaultHistory() {
        return new InMemoryHistoryManager<>();
    }
}
