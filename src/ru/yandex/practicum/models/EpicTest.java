package ru.yandex.practicum.models;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.enums.Status;
import ru.yandex.practicum.service.InMemoryTaskManager;
import ru.yandex.practicum.service.TaskManager;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    private static final TaskManager TASK_MANAGER = new InMemoryTaskManager();

    private static SubTask subtask;
    private static Epic epic;

    @BeforeAll
    public static void createTasksForTesting() { // создаем по 1 задаче для тестирования, чтобы id эпика был зафиксирован
        epic = new Epic("testEpic", "testDescription", 0, Status.NEW);
        TASK_MANAGER.createEpic(epic);

        subtask = new SubTask("testSubtask", "testDescription", 0, Status.NEW, 15,
                LocalDateTime.of(2023, Month.MARCH, 16, 14, 2), 1);
        TASK_MANAGER.createSubTask(subtask);
    }

    @Test
    public void epicStatusShouldWorkInEveryCase () { // статус эпика должен правильно считаться во всех случаях
        TASK_MANAGER.deleteSubTask(subtask.getUniqueID());

        assertEquals(0, epic.getSubtaskIds().size());
        assertEquals(Status.NEW, epic.getStatus()); // статус должен быть New при пустом списке

        TASK_MANAGER.createSubTask(subtask); // возвращаем подзадачу

        SubTask subTask1 = new SubTask("testSubtask1", "testDescription", 0,
                Status.NEW, 15, LocalDateTime.of(2023, Month.MARCH, 16, 14, 7), 1);
        SubTask subTask2 = new SubTask("testSubtask2", "testDescription", 0,
                Status.NEW, 15, LocalDateTime.of(2023, Month.MARCH, 16, 14, 22), 1);

        TASK_MANAGER.createSubTask(subTask1);
        TASK_MANAGER.createSubTask(subTask2);

        assertEquals(Status.NEW, epic.getStatus()); // статус должен быть New при всех подзадачах со статусом NEW

        subtask.setStatus(Status.DONE);
        subTask1.setStatus(Status.DONE);
        subTask2.setStatus(Status.DONE);

        TASK_MANAGER.updateSubTask(subtask);
        TASK_MANAGER.updateSubTask(subTask1);
        TASK_MANAGER.updateSubTask(subTask2);

        assertEquals(Status.DONE, epic.getStatus()); // статус должен быть DONE при всех подзадачах со статусом DONE

        subtask.setStatus(Status.NEW);
        subTask2.setStatus(Status.NEW);

        TASK_MANAGER.updateSubTask(subtask);
        TASK_MANAGER.updateSubTask(subTask2);

        assertEquals(Status.IN_PROGRESS, epic.getStatus()); // статус должен быть In_progress, когда 1 NEW, а вторая DONE

        subtask.setStatus(Status.IN_PROGRESS);
        subTask1.setStatus(Status.IN_PROGRESS);
        subTask2.setStatus(Status.IN_PROGRESS);

        TASK_MANAGER.updateSubTask(subtask);
        TASK_MANAGER.updateSubTask(subTask1);
        TASK_MANAGER.updateSubTask(subTask2);

        assertEquals(Status.IN_PROGRESS, epic.getStatus()); // статус должен быть In_progress, когда все In_progress
    }

    @Test
    public void sizeOfEpicSubtasksShouldEqual3 () { // размер списка подзадач у эпика должен быть равен 3
        assertEquals(3, epic.getSubtaskIds().size());
    }
}