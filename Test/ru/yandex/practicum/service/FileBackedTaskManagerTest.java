package ru.yandex.practicum.service;

import org.junit.jupiter.api.*;
import ru.yandex.practicum.enums.Status;
import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.Task;


import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    private File file;

    @BeforeEach // создаем по 1 задаче для тестирования, чтобы id эпика был зафиксирован
    public void createTasksForTesting() {
        file = new File("resources/test_" + System.nanoTime() + ".csv");
        super.setTaskManager(new FileBackedTaskManager(file.toString()));
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
    protected void tearDown() {
        assertTrue(file.delete());
    }

    @Test // должен выбрасывать исключение при загрузке из неправ файла
    public void shouldThrowExceptionWithWrongPath () {
        assertThrows(FileNotFoundException.class, () -> taskManager.loadFromFile(new File
                ("resources/wrongFile.csv")));
    }

    @Test // должен загружать из файла
    public void shouldLoadFromFile () throws FileNotFoundException {

        List<Task> historyTasks = taskManager.getHistoryManager().getHistory();

        assertEquals(2, historyTasks.size()); // подзадача вызывает эпик, метод апдейт вызывает подзадачу

        taskManager.getTask(1);

        historyTasks = taskManager.getHistoryManager().getHistory();

        assertEquals(3, historyTasks.size());

        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(new File(file.toString()));

        assertEquals(1, fileBackedTaskManager.getTasks().size());

        assertEquals(1, fileBackedTaskManager.getSubtasks().size());

        assertEquals(1, fileBackedTaskManager.getEpics().size());

        List<Task> tasksHistory = fileBackedTaskManager.getHistoryManager().getHistory();

        assertEquals(3, tasksHistory.size());
    }
}