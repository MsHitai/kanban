package ru.yandex.practicum.service;

import ru.yandex.practicum.models.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager<T extends Task> implements HistoryManager<T> {

    List<T> historyTasks = new LinkedList<>();

    @Override
    public void add(T task) {
        if (task == null) { // поняла, спасибо!
            return;
        }
        if (historyTasks.size() > 9) { // показалось, что если > 10 то в списке может быть 11
            historyTasks.remove(0);
        }
        historyTasks.add(task);

    }

    @Override
    public List<T> getHistory() {
        return historyTasks;
    }


}
