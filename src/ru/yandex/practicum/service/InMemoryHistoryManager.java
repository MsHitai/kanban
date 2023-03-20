package ru.yandex.practicum.service;

import ru.yandex.practicum.models.Task;

import java.util.*;

public class InMemoryHistoryManager<T extends Task> implements HistoryManager<T> {


    CustomLinkedList<Task> historyTasks = new CustomLinkedList<>();

    protected final Map<Integer, Node<Task>> sortingTasks = new HashMap<>(); // ошибка в истории была из-за static

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (sortingTasks.containsKey(task.getUniqueID())) {
            remove(task.getUniqueID());
        }
        historyTasks.linkLast(task);
        sortingTasks.put(task.getUniqueID(), historyTasks.tail);
    }

    @Override
    public void remove(int id) {
        Node<Task> node = sortingTasks.getOrDefault(id, null);
        historyTasks.removeNode(node);
        sortingTasks.remove(id);
    }


    @Override
    public List<Task> getHistory() {

        return historyTasks.getTasks();
    }

    private static class CustomLinkedList<T> {
        private Node<T> head;
        private Node<T> tail;

        public List<Task> getTasks() {
            List<Task> history = new ArrayList<>();
            Node<T> node = head;
            while (node != null) {
                history.add((Task) node.task);
                node = node.next;
            }
            return history;
        }

        public void linkLast(T task) {
            final Node<T> prev = tail;
            final Node<T> node = new Node<>(prev, task, null);

            tail = node;
            if (prev != null) {
                prev.next = node;
            } else {
                head = node;
            }
        }

        public void removeNode(Node<T> node) {
            if (node == null) {
                return;
            }
            if (node.prev != null) {
                node.prev.next = node.next;
                if (node.next == null) { // node == next
                    tail = node.prev;
                } else {
                    node.next.prev = node.prev;
                }
            } else {
                head = node.next;
                if (head == null) {
                    tail = null;
                } else {
                    head.prev = null;
                }
            }
        }
    }
}
