package ru.yandex.practicum.models;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.enums.Status;
import ru.yandex.practicum.service.InMemoryTaskManager;
import ru.yandex.practicum.service.TaskManager;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    private static final TaskManager TASK_MANAGER = new InMemoryTaskManager();

    private static SubTask subtask;
    private static SubTask subtask1;
    private static SubTask subtask2;
    private static Epic epic;

    @BeforeAll
    public static void createTasksForTesting() { // создаем по 1 задаче для тестирования, чтобы id эпика был зафиксирован
        epic = new Epic("testEpic", "testDescription", 0, Status.NEW);
        TASK_MANAGER.createEpic(epic);

        subtask = new SubTask("testSubtask", "testDescription", 0, Status.NEW, 15,
                LocalDateTime.of(2023, Month.MARCH, 16, 14, 2), 1);
        subtask1 = new SubTask("testSubtask1", "testDescription", 0,
                Status.NEW, 15, LocalDateTime.of(2023, Month.MARCH, 16, 14, 7), 1);
        subtask2 = new SubTask("testSubtask2", "testDescription", 0,
                Status.NEW, 15, LocalDateTime.of(2023, Month.MARCH, 16, 14, 22), 1);
        TASK_MANAGER.createSubTask(subtask);
        TASK_MANAGER.createSubTask(subtask1);
        TASK_MANAGER.createSubTask(subtask2);
    }

    @Test
    public void epicStatusShouldWorkInEveryCase () { // статус эпика должен правильно считаться во всех случаях
        TASK_MANAGER.deleteAllSubTasks();

        assertEquals(0, epic.getSubtaskIds().size());
        assertEquals(Status.NEW, epic.getStatus()); // статус должен быть New при пустом списке

        TASK_MANAGER.createSubTask(subtask); // возвращаем подзадачи
        TASK_MANAGER.createSubTask(subtask1);
        TASK_MANAGER.createSubTask(subtask2);

        assertEquals(Status.NEW, epic.getStatus()); // статус должен быть New при всех подзадачах со статусом NEW

        subtask.setStatus(Status.DONE);
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);

        TASK_MANAGER.updateSubTask(subtask);
        TASK_MANAGER.updateSubTask(subtask1);
        TASK_MANAGER.updateSubTask(subtask2);

        assertEquals(Status.DONE, epic.getStatus()); // статус должен быть DONE при всех подзадачах со статусом DONE

        subtask.setStatus(Status.NEW);
        subtask2.setStatus(Status.NEW);

        TASK_MANAGER.updateSubTask(subtask);
        TASK_MANAGER.updateSubTask(subtask2);

        assertEquals(Status.IN_PROGRESS, epic.getStatus()); // статус должен быть In_progress, когда 1 NEW, а вторая DONE

        subtask.setStatus(Status.IN_PROGRESS);
        subtask1.setStatus(Status.IN_PROGRESS);
        subtask2.setStatus(Status.IN_PROGRESS);

        TASK_MANAGER.updateSubTask(subtask);
        TASK_MANAGER.updateSubTask(subtask1);
        TASK_MANAGER.updateSubTask(subtask2);

        assertEquals(Status.IN_PROGRESS, epic.getStatus()); // статус должен быть In_progress, когда все In_progress
    }

    @Test
    public void sizeOfEpicSubtasksShouldEqual3 () { // размер списка подзадач у эпика должен быть равен 3
        assertEquals(3, epic.getSubtaskIds().size());
    }

    @Test
    public void startTimeOfEpicShouldBe14H07M () { // время начала эпика должно быть 14:07
        assertEquals(LocalDateTime.of(2023, Month.MARCH, 16, 14, 2), epic.getStartTime());
    }

    @Test
    public void endTimeOfEpicShouldBe14H37 () { // время конца эпика должно быть 14:35, по subTask2 времени
        assertEquals(LocalDateTime.of(2023, Month.MARCH, 16, 14, 37), epic.getEndTime());
    }

    @Test
    public void subtask1ShouldNotBeInPrioritizedTasks () { // в приоритетн.задачах не должно быть пересечения
        assertFalse(TASK_MANAGER.getPrioritizedTasks().contains(subtask1));
    }

    @Test
    public void prioritziedTasksSortNullAsLast () { // задачи без времени должны быть в конце по приоритету
        SubTask subTask4 = new SubTask("someName", "someDescription", 0, Status.NEW, 1);
        TASK_MANAGER.createSubTask(subTask4);

        List<Task> prioritized = new ArrayList<>(TASK_MANAGER.getPrioritizedTasks());

        assertEquals(3, prioritized.size());
        assertNull(prioritized.get(2).getStartTime());

        TASK_MANAGER.deleteSubTask(subTask4.getUniqueID());
        // удаление задачи, удаляет ее из списка приоритетных
        assertFalse(TASK_MANAGER.getPrioritizedTasks().contains(subTask4));
    }

}