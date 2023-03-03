package ru.yandex.practicum.service;

import ru.yandex.practicum.models.Task;

import java.util.*;

public class InMemoryHistoryManager<T extends Task> implements HistoryManager<T> {


    CustomLinkedList<Task> historyTasks = new CustomLinkedList<>();

    protected static Map<Integer, Node<Task>> sortingTasks = new HashMap<>();

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

    public static String historyToString(HistoryManager<Task> historyManager) {
        StringBuilder sb = new StringBuilder();
        for (Integer id : sortingTasks.keySet()) {
            sb.append(id);
            sb.append(",");
        }
        return sb.toString(); // зачем передавать параметр historyManager, если historyToString принадлежит классу?
        // Если брать id даже не из sortingTasks, которые тоже принадлежат классу, мы же не можем взять не статичный
        // список в статичном методе, верно?
    }

    public static List<Integer> historyFromString (String value) {
        List<Integer> list = new ArrayList<>();
        String[] ids = value.split(",");
        for (String i : ids) {
            list.add(Integer.parseInt(i));
        }

        return list;
    }

    private static class CustomLinkedList<T> {
        private int size;
        private Node<T> head;
        private Node<T> tail;

        public CustomLinkedList() {
            size = 0;
        }

        public List<Task> getTasks() {
            List<Task> history = new ArrayList<>();
            Node<T> node = head;
            for (int i = 0; i < size; i++) {
                history.add((Task) node.task);
                node = node.next;
            }
            return history;
        }

        public void linkLast(T task) {
            Node<T> node = new Node<>(null, task, null);

            if (size == 0) {
                this.head = node;
                this.tail = node;
            } else {
                node.prev = tail;
                tail.next = node;
                tail = node;
            }
            size++;
        }

        public void removeNode(Node<T> node) {
            if (node == null) {
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
                Node<T> before = node.prev;
                before.next = before.next.next;
                before.next.prev = before;
            }
            size--;
        }
    }
}
