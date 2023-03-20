package ru.yandex.practicum.service;

import org.junit.jupiter.api.*;
import ru.yandex.practicum.enums.Status;
import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.Task;

import java.time.LocalDateTime;
import java.time.Month;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach // создаем по 1 задаче для тестирования, чтобы id эпика был зафиксирован
    public void createTasksForTesting() {
        super.setTaskManager(new InMemoryTaskManager());
        task = new Task("testTask", "testDescription", 0, Status.NEW, 15,
                LocalDateTime.of(2023, Month.MARCH, 16, 15, 4));
        taskManager.createTask(task);

        epic = new Epic("testEpic", "testDescription", 0, Status.NEW);
        taskManager.createEpic(epic);

        subtask = new SubTask("testSubtask", "testDescription", 0, Status.NEW, 15,
                LocalDateTime.of(2023, Month.MARCH, 16, 14, 2), 2);
        taskManager.createSubTask(subtask);
    }

    @AfterEach
    public void garbageCollection () {
        taskManager.deleteAllTasks();
        taskManager.deleteAllSubTasks();
        taskManager.deleteAllEpics();
    }
}