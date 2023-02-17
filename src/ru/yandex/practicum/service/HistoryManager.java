package ru.yandex.practicum.service;

import ru.yandex.practicum.models.Task;

import java.util.List;

public interface HistoryManager<T extends Task> {
    void add(T task);

    void remove(int id);

    List<T> getHistory();
}
