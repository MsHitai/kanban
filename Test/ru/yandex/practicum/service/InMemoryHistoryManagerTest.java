package ru.yandex.practicum.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.enums.Status;
import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.Task;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private InMemoryTaskManager taskManager;
    private Task task;

    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
        task = new Task("testTask", "testDescription", 0, Status.NEW, 15,
                LocalDateTime.of(2023, Month.MARCH, 16, 15, 4));
        taskManager.createTask(task);
    }

    @Test
    public void addRemoveNodeCheck() {
        taskManager.getTask(task.getUniqueID());
        List<Task> historyTasks = taskManager.getHistoryManager().getHistory();

        assertEquals(1, historyTasks.size());

        Epic epic = new Epic("testEpic", "testDescription", 0, Status.NEW);
        taskManager.createEpic(epic);

        taskManager.getEpic(epic.getUniqueID());

        historyTasks = taskManager.getHistoryManager().getHistory();
        assertEquals(2, historyTasks.size());

        SubTask subtask = new SubTask("testSubtask", "testDescription", 0, Status.NEW, 15,
                LocalDateTime.of(2023, Month.MARCH, 16, 14, 2), 2);
        taskManager.createSubTask(subtask);

        historyTasks = taskManager.getHistoryManager().getHistory();
        assertEquals(3, historyTasks.size()); // подзадача вызывает getEpic(),а в updateStatus вызывается getSubtask()

        HistoryManager<Task> historyManager = taskManager.getHistoryManager();

        historyManager.remove(2);

        historyTasks = taskManager.getHistoryManager().getHistory();
        assertEquals(2, historyTasks.size());

        epic = new Epic("testEpic", "testDescription", 0, Status.NEW);
        taskManager.createEpic(epic);

        subtask = new SubTask("testSubtask", "testDescription", 0, Status.NEW, 15,
                LocalDateTime.of(2023, Month.MARCH, 16, 14, 2), 2);
        taskManager.createSubTask(subtask);

        taskManager.deleteEpic(2);

        historyTasks = taskManager.getHistoryManager().getHistory();
        assertEquals(1, historyTasks.size());
    }
}