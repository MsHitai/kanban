package ru.yandex.practicum.service;

import ru.yandex.practicum.models.Task;

public class Node<T extends Task> {
    T task;
    Node<T> next;
    Node<T> prev;

    public Node(Node<T> prev, T task, Node<T> next) {
        this.task = task;
        this.next = next;
        this.prev = prev;
    }

}
