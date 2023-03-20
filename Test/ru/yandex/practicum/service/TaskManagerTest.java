package ru.yandex.practicum.service;

import org.junit.jupiter.api.Assertions;
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

abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    protected Task task;
    protected SubTask subtask;
    protected Epic epic;

    public void setTaskManager(T taskManager) {
        this.taskManager = taskManager;
    }

    @Test // можно создавать 1 задачу
    public void shouldCreateOneTask() {
        Assertions.assertNotNull(taskManager.getTask(task.getUniqueID()), "Задача не найдена.");
        Assertions.assertEquals(task, taskManager.getTask(task.getUniqueID()), "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test // можно создавать 1 эпик
    public void shouldCreateOneEpic() {
        Assertions.assertNotNull(taskManager.getEpic(epic.getUniqueID()), "Задача не найдена.");
        Assertions.assertEquals(epic, taskManager.getEpic(epic.getUniqueID()), "Задачи не совпадают.");

        final List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Задачи на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
    }

    @Test // можно создавать 1 подзадачу
    public void shouldCreateOneSubtask() {
        Assertions.assertNotNull(taskManager.getSubtask(subtask.getUniqueID()), "Задача не найдена.");
        Assertions.assertEquals(subtask, taskManager.getSubtask(subtask.getUniqueID()), "Задачи не совпадают.");

        final List<SubTask> subTasks = taskManager.getSubtasks();

        assertNotNull(subTasks, "Задачи на возвращаются.");
        assertEquals(1, subTasks.size(), "Неверное количество задач.");
        assertEquals(subtask, subTasks.get(0), "Задачи не совпадают.");
    }

    @Test // подзадача должна быть раньше по времени/приоритету
    public void subtaskShouldBeFirstInPrioritizedTasks() {
        List<Task> prioritized = new ArrayList<>(taskManager.getPrioritizedTasks());

        assertEquals(2, prioritized.size(), "Неверное количество задач.");
        assertEquals(subtask, prioritized.get(0), "Задачи не совпадают.");
    }

    @Test // должно быть возможным обновлять статус у задачи
    public void shouldUpdateTaskStatus() {
        Task task1 = new Task("testTask", "testDescription", task.getUniqueID(), Status.DONE, 15,
                LocalDateTime.of(2023, Month.MARCH, 16, 15, 4));
        taskManager.updateTask(task1);
        Task task2 = taskManager.getTask(task1.getUniqueID());

        assertEquals(Status.DONE, task2.getStatus(), "Статус не совпадает");
    }

    @Test // должно быть возможным обновлять статус у подзадачи
    public void shouldUpdateSubtaskStatus() {
        SubTask subTask1 = new SubTask("testSubtask", "testDescription", subtask.getUniqueID(),
                Status.IN_PROGRESS, 15, LocalDateTime.of(2023, Month.MARCH, 16, 14, 2), 2);
        taskManager.updateSubTask(subTask1);

        SubTask subTask2 = taskManager.getSubtask(subTask1.getUniqueID());

        assertEquals(Status.IN_PROGRESS, subTask2.getStatus(), "Статус не совпадает");
    }

    @Test // нельзя поменять статус Эпика вручную
    public void shouldNotBeAbleToChangeEpicStatusManually () {
        Epic epic1 = new Epic("testEpic", "testDescription", epic.getUniqueID(), Status.DONE);
        taskManager.updateEpic(epic1);

        Epic epic2 = taskManager.getEpic(epic1.getUniqueID());

        assertNotEquals(Status.DONE, epic2.getStatus(), "Статус поменялся");
    }

    @Test // измененный статус подзадачи меняет статус Эпика
    public void changedStatusOfOneSubtaskShouldChangeEpicStatus() {
        SubTask subTask1 = new SubTask("testSubtask", "testDescription", 0,
                Status.NEW, 15, LocalDateTime.of(2023, Month.MARCH, 16, 14, 2), 2);
        taskManager.createSubTask(subTask1);
        subTask1.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubTask(subTask1);
        Epic epic1 = taskManager.getEpic(epic.getUniqueID());

        assertEquals(Status.IN_PROGRESS, epic1.getStatus(), "Статус не совпадает");

        taskManager.deleteSubTask(subTask1.getUniqueID()); // удаляем подзадачу, проверяем вернулся ли статус эпика

        assertEquals(Status.NEW, epic1.getStatus(), "Статус не совпадает");
    }

    @Test // не должно обновлять задачу с неправильным id
    public void shouldNotUpdateTaskWithWrongId () {
        Task task1 = new Task("testTask", "testDescription", 0, Status.IN_PROGRESS, 15,
                LocalDateTime.of(2023, Month.MARCH, 16, 15, 4));
        taskManager.updateTask(task1);

        assertNotEquals(Status.IN_PROGRESS, task.getStatus(), "Статусы совпадают.");
    }

    @Test // не должно обновлять подзадачу с неправильным id
    public void shouldNotUpdateSubtaskWithWrongId () {
        SubTask subTask1 = new SubTask("testSubtask", "testDescription", 0, Status.NEW, 15,
                LocalDateTime.of(2023, Month.MARCH, 16, 14, 2), 2);
        taskManager.updateSubTask(subTask1);

        assertNotEquals(Status.IN_PROGRESS, subtask.getStatus(), "Статусы совпадают.");
    }


    @Test // не должно обновлять эпик с неправильным id
    public void shouldNotUpdateEpicWithWrongId () {
        Epic epic1 = new Epic("changeName", "testDescription", 0, Status.DONE);
        taskManager.updateEpic(epic1);

        assertNotEquals(Status.DONE, epic.getStatus(), "Статусы совпадают.");
    }

    @Test // должны получить null task с неправильным id
    public void shouldGetNullTaskWithWrongId () {
        assertNull(taskManager.getTask(0));
    }

    @Test // должны получить null subtask с неправильным id
    public void shouldGetNullSubtaskWithWrongId () {
        assertNull(taskManager.getSubtask(0));
    }

    @Test // должны получить null epic с неправильным id
    public void shouldGetNullEpicWithWrongId () {
        assertNull(taskManager.getEpic(0));
    }

    @Test // должны получить одинаковые задачи по правильному id
    public void shouldGetSameTaskByCorrectId () {
        assertEquals(task, taskManager.getTask(task.getUniqueID()), "Задачи отличаются");
    }

    @Test // должны получить одинаковые подзадачи по правильному id
    public void shouldGetSameSubtaskByCorrectId () {
        assertEquals(subtask, taskManager.getSubtask(subtask.getUniqueID()), "Задачи отличаются");
    }

    @Test // по информации подзадачи эпик id должен совпадать с эпиком в памяти
    public void epicIdShouldBeSameBySubtaskInfo () {
        Epic epic1 = taskManager.getEpic(subtask.getEpicId());

        assertEquals(epic, epic1);
    }

    @Test // должны получить одинаковые эпики по правильному id
    public void shouldGetSameEpicByCorrectId () {
        assertEquals(epic, taskManager.getEpic(epic.getUniqueID()), "Задачи отличаются");
    }

    @Test // не должен удалять задачу с неправильным id
    public void shouldNotDeleteTaskWithWrongId () {
        taskManager.deleteTask(0);
        assertNotNull(task, "Задача удалилась!");
    }

    @Test // не должен удалять подзадачу с неправильным id
    public void shouldNotDeleteSubtaskWithWrongId () {
        taskManager.deleteSubTask(0);
        assertNotNull(subtask, "Подзадача удалилась!");
    }

    @Test // не должен удалять эпик с неправильным id
    public void shouldNotDeleteEpicWithWrongId () {
        taskManager.deleteEpic(0);
        assertNotNull(epic, "Эпик удалился!");
    }

    @Test // должен удалять задачи
    public void shouldDeleteTasks() {
        taskManager.deleteTask(task.getUniqueID());

        assertEquals(0, taskManager.getTasks().size());

        taskManager.createTask(task); // возвращаем задачу для проверки удаления всех задач
        Task task1 = new Task("testTask", "testDescription", 0, Status.NEW, 15,
                LocalDateTime.of(2023, Month.MARCH, 16, 15, 4));
        taskManager.createTask(task1);

        assertEquals(2, taskManager.getTasks().size());

        taskManager.deleteAllTasks();

        assertEquals(0, taskManager.getTasks().size());
    }

    @Test // должен удалять подзадачи
    public void shouldDeleteSubtasks() {
        taskManager.deleteSubTask(subtask.getUniqueID());

        assertEquals(0, taskManager.getSubtasks().size());

        taskManager.createSubTask(subtask); // возвращаем подзадачу для проверки удаления всех подзадач
        SubTask subTask1 = new SubTask("testSubtask", "testDescription", 0, Status.NEW, 15,
                LocalDateTime.of(2023, Month.MARCH, 16, 14, 2), 2);
        taskManager.createSubTask(subTask1);

        assertEquals(2, taskManager.getSubtasks().size());

        taskManager.deleteAllSubTasks();
        assertEquals(0, taskManager.getSubtasks().size());
    }

    @Test // должен удалять эпик
    public void shouldDeleteEpics() {
        taskManager.deleteEpic(epic.getUniqueID());

        assertEquals(0, taskManager.getEpics().size());
        assertEquals(0, epic.getSubtaskIds().size());
        assertEquals(0, taskManager.getSubtasks().size());

        taskManager.createEpic(epic); // возвращаем эпик для следующих тестов
        Epic epic1 = new Epic("testEpic", "testDescription", 0, Status.NEW);
        taskManager.createEpic(epic1);

        assertEquals(2, taskManager.getEpics().size());

        taskManager.deleteAllEpics();
        assertEquals(0, taskManager.getEpics().size());

        assertEquals(0, epic.getSubtaskIds().size());
        assertEquals(0, taskManager.getSubtasks().size());
    }
}