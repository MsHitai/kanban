package ru.yandex.practicum.service;

import ru.yandex.practicum.models.Task;

import java.util.*;

public class InMemoryHistoryManager<T extends Task> implements HistoryManager<T> {


    CustomLinkedList<T> historyTasks = new CustomLinkedList<>();

    @Override
    public void add(T task) {
        if (task == null) {
            return;
        }
        if (historyTasks.sortingTasks.containsKey(task.getUniqueID())) {
            remove(task.getUniqueID());
        }
        historyTasks.linkLast(task);
    }

    @Override
    public void remove(int id) {
        Node node = historyTasks.getNode(id);
        historyTasks.removeNode(node);
    }


    @Override
    public List<T> getHistory() {

        return historyTasks.getTasks();
    }

    private static class CustomLinkedList<T extends Task> {
        private int size;
        private Node head;
        private Node tail;
        private final Map<Integer, Node> sortingTasks = new HashMap<>();

        private void addFirst(T task) {
            Node node = new Node(task);
            node.next = head;
            node.prev = null;
            if (head != null) {
                head.prev = node;
            }
            head = node;
            sortingTasks.put(task.getUniqueID(), head);
            size++;
        }

        public List<T> getTasks() {
            List<T> history = new ArrayList<>();
            Node node = head;
            for (int i = 0; i < size; i++) {
                history.add((T) node.task);
                node = node.next;
            }
            return history;
        }

        public void linkLast(T task) {
            Node node = new Node(task);
            node.next = null;
            node.prev = tail;
            if (tail == null) {
                addFirst(task);
                return;
            }
            tail.next = node;
            tail = node;
            sortingTasks.put(task.getUniqueID(), tail);
            size++;
        }

        private void removeFirst() {
            removeNodefromMap(head);
            head = head.next;
            if (head == null) {
                tail = null;
            }
            size--;
        }

        private void removeLast() {
            if (size <= 1) {
                removeFirst();
                return;
            }
            tail = tail.prev;
            tail.next = null;
            removeNodefromMap(tail);
            size--;
        }

        public Node getNode(int id) {
            for (Map.Entry<Integer, Node> entry : sortingTasks.entrySet()) {
                int taskId = entry.getKey();
                Node node = entry.getValue();
                if (id == taskId) {
                    return node;
                }
            }
            return null;
        }

        public void removeNode(Node node) {
            if (node == null) {
                return;
            }
            if (node == head) {
                removeFirst();
                return;
            } else if (node == tail) {
                removeLast();
                return;
            }
            Node node1 = node.prev;
            node1.next = node.next;
            removeNodefromMap(node);
            size--;
        }

        private void removeNodefromMap(Node node) {
            for (Map.Entry<Integer, Node> entry : sortingTasks.entrySet()) {
                int taskId = entry.getKey();
                Node node1 = entry.getValue();
                if (node1 == node) {
                    sortingTasks.remove(taskId);
                    return;
                }
            }

        }
    }
}
