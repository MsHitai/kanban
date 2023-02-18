package ru.yandex.practicum.service;

import ru.yandex.practicum.models.Task;

import java.util.*;

public class InMemoryHistoryManager<T extends Task> implements HistoryManager<T> {


    CustomLinkedList<T> historyTasks = new CustomLinkedList<>();

    protected static Map<Integer, Node> sortingTasks = new HashMap<>(); // как раз сомневалась, где она более логична
                                                                        // и уместна:)))

    @Override
    public void add(T task) {
        if (task == null) {
            return;
        }
        if (sortingTasks.containsKey(task.getUniqueID())) {
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

        public CustomLinkedList() {
            size = 0;
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

            if (size == 0) {
                this.head = node;
                this.tail = node;
            } else {
                node.prev = tail;
                tail.next = node;
                tail = node;
            }
            sortingTasks.put(task.getUniqueID(), node);
            size++;
        }

        public Node getNode(int id) {
            return sortingTasks.getOrDefault(id, null);
        }

        public void removeNode(Node node) {
            if (node == null) {
                removeNodefromMap(node);
                return;
            }
            if (size == 1) {
                head = null;
                tail = null;
            } else if (node == head) {
                head = head.next;
                head.prev = null;
            } else if (node == tail) {
                tail = tail.prev;
                tail.next = null;
            } else {
                Node before = node.prev;
                before.next = before.next.next;
                before.next.prev = before;
            }
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
