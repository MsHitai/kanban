package ru.yandex.practicum.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.enums.Status;
import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.Task;
import ru.yandex.practicum.server.KVServer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {

    KVServer kvServer = new KVServer();

    HttpTaskManagerTest() throws IOException {
    }

    @BeforeEach
    void setUp() throws URISyntaxException {
        kvServer.start();
        super.setTaskManager(new HttpTaskManager("http://localhost:8078/"));
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
    void tearDown() {
        kvServer.stop();
    }

    @Test
    void shouldLoadFromServer() throws URISyntaxException {
        HttpTaskManager newTaskManager = new HttpTaskManager("http://localhost:8078/");

        assertEquals(1, newTaskManager.tasks.size());
        assertEquals(1, newTaskManager.subtasks.size());
        assertEquals(1, newTaskManager.epics.size());

        assertEquals(2, newTaskManager.getHistory().size());
    }
}