import org.junit.jupiter.api.*;
import ru.yandex.practicum.enums.Status;
import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.Task;
import ru.yandex.practicum.service.FileBackedTaskManager;
import ru.yandex.practicum.service.HistoryManager;
import ru.yandex.practicum.service.InMemoryHistoryManager;


import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.Month;

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
        taskManager = FileBackedTaskManager.loadFromFile(new File(file.toString()));

        assertEquals(1, taskManager.getTasks().size());

        taskManager.getTask(1);

        assertEquals(1, taskManager.getSubtasks().size());

        taskManager.getSubtask(3);

        taskManager.getEpic(2);

        HistoryManager<Task> historyManager = new InMemoryHistoryManager<>();

        assertEquals(3, historyManager.getHistory().size());


    }
}