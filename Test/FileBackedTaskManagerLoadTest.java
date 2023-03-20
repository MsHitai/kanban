import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerLoadTest {

    private static FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager("resources/save2.csv");

    @BeforeAll
    public static void shouldLoadFromFile () throws FileNotFoundException { // должен загружать из файла
        fileBackedTaskManager = FileBackedTaskManager.loadFromFile(
                new File("resources/save2.csv"));
    }

    @Test
    public void shouldBeOneSameTask () { // должна быть одна задача после загрузки
        assertEquals(1, fileBackedTaskManager.getTasks().size());

        Task task1 = new Task("testTask", "testDescription", 1, Status.NEW, 15,
                LocalDateTime.of(2023, Month.MARCH, 16, 15, 4));

        assertEquals(task1, fileBackedTaskManager.getTask(1));
    }

    @Test
    public void shouldBeOneSameSubtask () { // должна быть одна подзадача после загрузки
        assertEquals(1, fileBackedTaskManager.getSubtasks().size());

        SubTask subTask = new SubTask("testSubtask", "testDescription", 3,
                Status.NEW, 15, LocalDateTime.of(2023, Month.MARCH, 16, 14, 2), 2);

        assertEquals(subTask, fileBackedTaskManager.getSubtask(3));
    }

    @Test
    public void shouldBeOneSameEpic () { // должен быть один эпик после загрузки
        assertEquals(1, fileBackedTaskManager.getEpics().size());

        Epic epic1 = new Epic("testEpic", "testDescription", 2, Status.NEW);
        epic1.setStartTime(LocalDateTime.of(2023, Month.MARCH, 16, 14, 2));
        epic1.setDuration(15);
        epic1.setEndTime(LocalDateTime.of(2023, Month.MARCH, 16, 14, 17));

        assertEquals(epic1, fileBackedTaskManager.getEpic(2));
    }

    @Test
    public void shouldBeThreeTasksInHistory () { // должно быть 3 задачи в истории с нужными id
        HistoryManager<Task> historyManager = new InMemoryHistoryManager<>();

        assertEquals(3, historyManager.getHistory().size());

        int[] idsOfTasks = { 2, 1, 3};

        int[] idsFromHistory = new int[idsOfTasks.length];

        List<Task> tasksInHistory = historyManager.getHistory();
        int i = 0;

        for (Task task : tasksInHistory) {
            idsFromHistory[i] = task.getUniqueID();
            i++;
        }

        assertArrayEquals(idsOfTasks, idsFromHistory);
    }
}