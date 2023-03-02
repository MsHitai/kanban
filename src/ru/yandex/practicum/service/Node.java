package ru.yandex.practicum.service;

public class Node<T> {
    T task;
    Node<T> next;
    Node<T> prev;

    public Node(Node<T> prev, T task, Node<T> next) {
        this.task = task;
        this.next = next;
        this.prev = prev;
    }

}
