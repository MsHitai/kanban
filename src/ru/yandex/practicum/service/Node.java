package ru.yandex.practicum.service;

import ru.yandex.practicum.models.Task;

public class Node {
    Task task;
    Node next;
    Node prev;

    public Node(Task task) {
        this.task = task;
    }

}
