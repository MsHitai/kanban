package ru.yandex.practicum.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.enums.Status;
import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.Task;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest<T extends TaskManager> {

    protected static final TaskManager TASK_MANAGER = Managers.getDefault();

    protected static Task task;
    protected static SubTask subtask;
    protected static Epic epic;

    @BeforeAll
    public static void createTasksForTesting() { // создаем по 1 задаче для тестирования, чтобы id эпика был зафиксирован
        task = new Task("testTask", "testDescription", 0, Status.NEW, 15,
                LocalDateTime.of(2023, Month.MARCH, 16, 15, 4));
        TASK_MANAGER.createTask(task);

        epic = new Epic("testEpic", "testDescription", 0, Status.NEW);
        TASK_MANAGER.createEpic(epic);

        subtask = new SubTask("testSubtask", "testDescription", 0, Status.NEW, 15,
                LocalDateTime.of(2023, Month.MARCH, 16, 14, 2), 2);
        TASK_MANAGER.createSubTask(subtask);
    }

    @Test
    public void shouldCreateOneTask() { // можно создавать 1 задачу
        assertNotNull(TASK_MANAGER.getTask(task.getUniqueID()), "Задача не найдена.");
        assertEquals(task, TASK_MANAGER.getTask(task.getUniqueID()), "Задачи не совпадают.");

        final List<Task> tasks = TASK_MANAGER.getTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void shouldCreateOneEpic() { // можно создавать 1 эпик
        assertNotNull(TASK_MANAGER.getEpic(epic.getUniqueID()), "Задача не найдена.");
        assertEquals(epic, TASK_MANAGER.getEpic(epic.getUniqueID()), "Задачи не совпадают.");

        final List<Epic> epics = TASK_MANAGER.getEpics();

        assertNotNull(epics, "Задачи на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    public void shouldCreateOneSubtask() { // можно создавать 1 подзадачу
        assertNotNull(TASK_MANAGER.getSubtask(subtask.getUniqueID()), "Задача не найдена.");
        assertEquals(subtask, TASK_MANAGER.getSubtask(subtask.getUniqueID()), "Задачи не совпадают.");

        final List<SubTask> subTasks = TASK_MANAGER.getSubtasks();

        assertNotNull(subTasks, "Задачи на возвращаются.");
        assertEquals(1, subTasks.size(), "Неверное количество задач.");
        assertEquals(subtask, subTasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void subtaskShouldBeFirstInPrioritizedTasks() { // подзадача должна быть раньше по времени/приоритету
        List<Task> prioritized = new ArrayList<>(TASK_MANAGER.getPrioritizedTasks());

        assertEquals(2, prioritized.size(), "Неверное количество задач.");
        assertEquals(subtask, prioritized.get(0), "Задачи не совпадают.");
    }

    @Test
    public void shouldUpdateTaskStatus() { // должно быть возможным обновлять статус у задачи
        Task task1 = new Task("testTask", "testDescription", task.getUniqueID(), Status.DONE, 15,
                LocalDateTime.of(2023, Month.MARCH, 16, 15, 4));
        TASK_MANAGER.updateTask(task1);
        Task task2 = TASK_MANAGER.getTask(task1.getUniqueID());

        assertEquals(Status.DONE, task2.getStatus(), "Статус не совпадает");
    }

    @Test
    public void shouldUpdateSubtaskStatus() { // должно быть возможным обновлять статус у подзадачи
        SubTask subTask1 = new SubTask("testSubtask", "testDescription", subtask.getUniqueID(),
                Status.IN_PROGRESS, 15, LocalDateTime.of(2023, Month.MARCH, 16, 14, 2), 2);
        TASK_MANAGER.updateSubTask(subTask1);

        SubTask subTask2 = TASK_MANAGER.getSubtask(subTask1.getUniqueID());

        assertEquals(Status.IN_PROGRESS, subTask2.getStatus(), "Статус не совпадает");
    }

    @Test
    public void shouldNotBeAbleToChangeEpicStatusManually () { // нельзя поменять статус Эпика вручную
        Epic epic1 = new Epic("testEpic", "testDescription", epic.getUniqueID(), Status.DONE);
        TASK_MANAGER.updateEpic(epic1);

        Epic epic2 = TASK_MANAGER.getEpic(epic1.getUniqueID());

        assertNotEquals(Status.DONE, epic2.getStatus(), "Статус поменялся");
    }

    @Test
    public void changedStatusOfOneSubtaskShouldChangeEpicStatus() { // измененный статус подзадачи меняет статус Эпика
        SubTask subTask1 = new SubTask("testSubtask", "testDescription", 0,
                Status.NEW, 15, LocalDateTime.of(2023, Month.MARCH, 16, 14, 2), 2);
        TASK_MANAGER.createSubTask(subTask1);
        subTask1.setStatus(Status.IN_PROGRESS);
        TASK_MANAGER.updateSubTask(subTask1);
        Epic epic1 = TASK_MANAGER.getEpic(epic.getUniqueID());

        assertEquals(Status.IN_PROGRESS, epic1.getStatus(), "Статус не совпадает");

        TASK_MANAGER.deleteSubTask(subTask1.getUniqueID());
    }

    @Test
    public void shouldGetNullTaskWithWrongId () { // должны получить null task с неправильным id
        assertNull(TASK_MANAGER.getTask(0));
    }
}