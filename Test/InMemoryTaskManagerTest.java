import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.enums.Status;
import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.Task;
import ru.yandex.practicum.service.TaskManager;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<TaskManager> {

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
        Assertions.assertNotNull(TASK_MANAGER.getTask(task.getUniqueID()), "Задача не найдена.");
        Assertions.assertEquals(task, TASK_MANAGER.getTask(task.getUniqueID()), "Задачи не совпадают.");

        final List<Task> tasks = TASK_MANAGER.getTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void shouldCreateOneEpic() { // можно создавать 1 эпик
        Assertions.assertNotNull(TASK_MANAGER.getEpic(epic.getUniqueID()), "Задача не найдена.");
        Assertions.assertEquals(epic, TASK_MANAGER.getEpic(epic.getUniqueID()), "Задачи не совпадают.");

        final List<Epic> epics = TASK_MANAGER.getEpics();

        assertNotNull(epics, "Задачи на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    public void shouldCreateOneSubtask() { // можно создавать 1 подзадачу
        Assertions.assertNotNull(TASK_MANAGER.getSubtask(subtask.getUniqueID()), "Задача не найдена.");
        Assertions.assertEquals(subtask, TASK_MANAGER.getSubtask(subtask.getUniqueID()), "Задачи не совпадают.");

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

        task1.setStatus(Status.NEW);
        TASK_MANAGER.updateTask(task1); // возвращаем статус на NEW, чтобы не было пересечений статусов с др.тестами
    }

    @Test
    public void shouldUpdateSubtaskStatus() { // должно быть возможным обновлять статус у подзадачи
        SubTask subTask1 = new SubTask("testSubtask", "testDescription", subtask.getUniqueID(),
                Status.IN_PROGRESS, 15, LocalDateTime.of(2023, Month.MARCH, 16, 14, 2), 2);
        TASK_MANAGER.updateSubTask(subTask1);

        SubTask subTask2 = TASK_MANAGER.getSubtask(subTask1.getUniqueID());

        assertEquals(Status.IN_PROGRESS, subTask2.getStatus(), "Статус не совпадает");

        subTask1.setStatus(Status.NEW);
        TASK_MANAGER.updateSubTask(subTask1); // возвращаем статус на NEW, чтобы не было пересечений статусов с др.тестами
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

        TASK_MANAGER.deleteSubTask(subTask1.getUniqueID()); // удаляем подзадачу, проверяем вернулся ли статус эпика

        assertEquals(Status.NEW, epic1.getStatus(), "Статус не совпадает");
    }

    @Test
    public void shouldNotUpdateTaskWithWrongId () { // не должно обновлять задачу с неправильным id
        Task task1 = new Task("testTask", "testDescription", 0, Status.IN_PROGRESS, 15,
                LocalDateTime.of(2023, Month.MARCH, 16, 15, 4));
        TASK_MANAGER.updateTask(task1);

        assertNotEquals(Status.IN_PROGRESS, task.getStatus(), "Статусы совпадают.");
    }

    @Test
    public void shouldNotUpdateSubtaskWithWrongId () { // не должно обновлять подзадачу с неправильным id
        SubTask subTask1 = new SubTask("testSubtask", "testDescription", 0, Status.NEW, 15,
                LocalDateTime.of(2023, Month.MARCH, 16, 14, 2), 2);
        TASK_MANAGER.updateSubTask(subTask1);

        assertNotEquals(Status.IN_PROGRESS, subtask.getStatus(), "Статусы совпадают.");
    }

    @Test
    public void shouldNotUpdateEpicWithWrongId () { // не должно обновлять эпик с неправильным id
        Epic epic1 = new Epic("changeName", "testDescription", 0, Status.DONE);
        TASK_MANAGER.updateEpic(epic1);

        assertNotEquals(Status.DONE, epic.getStatus(), "Статусы совпадают.");
    }

    @Test
    public void shouldGetNullTaskWithWrongId () { // должны получить null task с неправильным id

        assertNull(TASK_MANAGER.getTask(0));
    }

    @Test
    public void shouldGetNullSubtaskWithWrongId () { // должны получить null subtask с неправильным id

        assertNull(TASK_MANAGER.getSubtask(0));
    }

    @Test
    public void shouldGetNullEpicWithWrongId () { // должны получить null epic с неправильным id

        assertNull(TASK_MANAGER.getEpic(0));
    }

    @Test
    public void shouldGetSameTaskByCorrectId () { // должны получить одинаковые задачи по правильному id
        Assertions.assertEquals(task, TASK_MANAGER.getTask(task.getUniqueID()), "Задачи отличаются");
    }

    @Test
    public void shouldGetSameSubtaskByCorrectId () { // должны получить одинаковые подзадачи по правильному id
        Assertions.assertEquals(subtask, TASK_MANAGER.getSubtask(subtask.getUniqueID()), "Задачи отличаются");
    }

    @Test
    public void epicIdShouldBeSameBySubtaskInfo () { // по инфо подзадачи эпик id должен совпадать с эпиком в памяти
        Epic epic1 = TASK_MANAGER.getEpic(subtask.getEpicId());

        assertEquals(epic, epic1);
    }

    @Test
    public void shouldGetSameEpicByCorrectId () { // должны получить одинаковые эпики по правильному id
        Assertions.assertEquals(epic, TASK_MANAGER.getEpic(epic.getUniqueID()), "Задачи отличаются");
    }

    @Test
    public void shouldNotDeleteTaskWithWrongId () { // не должен удалять задачу с неправильным id
        TASK_MANAGER.deleteTask(0);
        assertNotNull(task, "Задача удалилась!");
    }

    @Test
    public void shouldNotDeleteSubtaskWithWrongId () { // не должен удалять подзадачу с неправильным id
        TASK_MANAGER.deleteSubTask(0);
        assertNotNull(subtask, "Подзадача удалилась!");
    }

    @Test
    public void shouldNotDeleteEpicWithWrongId () { // не должен удалять эпик с неправильным id
        TASK_MANAGER.deleteEpic(0);
        assertNotNull(epic, "Эпик удалился!");
    }

    @AfterAll
    public static void shouldDeleteTasks() { // объединяем тест на удаление задач, чтобы не нарушать очередность тестов
        TASK_MANAGER.deleteTask(task.getUniqueID());

        Assertions.assertEquals(0, TASK_MANAGER.getTasks().size());

        TASK_MANAGER.createTask(task); // возвращаем задачу для проверки удаления всех задач
        Task task1 = new Task("testTask", "testDescription", 0, Status.NEW, 15,
                LocalDateTime.of(2023, Month.MARCH, 16, 15, 4));
        TASK_MANAGER.createTask(task1);

        Assertions.assertEquals(2, TASK_MANAGER.getTasks().size());

        TASK_MANAGER.deleteAllTasks();

        Assertions.assertEquals(0, TASK_MANAGER.getTasks().size());
    }

    @AfterAll
    public static void shouldDeleteSubtasks() { // объединяем тест на удаление подзадач
        TASK_MANAGER.deleteSubTask(subtask.getUniqueID());

        Assertions.assertEquals(0, TASK_MANAGER.getSubtasks().size());

        TASK_MANAGER.createSubTask(subtask); // возвращаем подзадачу для следующих тестов
        SubTask subTask1 = new SubTask("testSubtask", "testDescription", 0, Status.NEW, 15,
                LocalDateTime.of(2023, Month.MARCH, 16, 14, 2), 2);
        TASK_MANAGER.createSubTask(subTask1);

        Assertions.assertEquals(2, TASK_MANAGER.getSubtasks().size());

        TASK_MANAGER.deleteAllSubTasks();
        Assertions.assertEquals(0, TASK_MANAGER.getSubtasks().size());
    }

    @AfterAll
    public static void shouldDeleteEpics() { // объединяем тест на удаление эпиков, проверяем удаление подзадач
        TASK_MANAGER.deleteEpic(epic.getUniqueID());

        Assertions.assertEquals(0, TASK_MANAGER.getEpics().size());
        assertEquals(0, epic.getSubtaskIds().size());
        Assertions.assertEquals(0, TASK_MANAGER.getSubtasks().size());

        TASK_MANAGER.createEpic(epic); // возвращаем эпик для следующих тестов
        Epic epic1 = new Epic("testEpic", "testDescription", 0, Status.NEW);
        TASK_MANAGER.createEpic(epic1);

        Assertions.assertEquals(2, TASK_MANAGER.getEpics().size());

        TASK_MANAGER.deleteAllEpics();
        Assertions.assertEquals(0, TASK_MANAGER.getEpics().size());

        assertEquals(0, epic.getSubtaskIds().size());
        Assertions.assertEquals(0, TASK_MANAGER.getSubtasks().size());
    }

}