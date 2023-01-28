package ru.yandex.practicum.service;

import ru.yandex.practicum.models.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager<T extends Task> implements HistoryManager<T> {

    List<T> historyTasks = new ArrayList<>();

    @Override
    public void add(T task) {
        if (task != null) { // чтобы не выводил Null в списке вызванных задач
            if (historyTasks.size() < 10) {
                historyTasks.add(task);
            } else {
                historyTasks.remove(0);
                historyTasks.add(task);
            }
        }
    }

    @Override
    public List<T> getHistory() {
        return historyTasks;
    }


}
