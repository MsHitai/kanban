package ru.yandex.practicum.service;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.Task;

abstract class TaskManagerTest<T extends TaskManager> {

    protected static final TaskManager TASK_MANAGER = Managers.getDefault();

    protected static Task task;
    protected static SubTask subtask;
    protected static Epic epic;

    @Test
    public abstract void shouldCreateOneTask(); // можно создавать 1 задачу

    @Test
    public abstract void shouldCreateOneEpic(); // можно создавать 1 эпик

    @Test
    public abstract void shouldCreateOneSubtask(); // можно создавать 1 подзадачу

    @Test
    public abstract void subtaskShouldBeFirstInPrioritizedTasks(); // подзадача должна быть раньше по времени/приоритету

    @Test
    public abstract void shouldUpdateTaskStatus(); // должно быть возможным обновлять статус у задачи

    @Test
    public abstract void shouldUpdateSubtaskStatus(); // должно быть возможным обновлять статус у подзадачи

    @Test
    public abstract void shouldNotBeAbleToChangeEpicStatusManually (); // нельзя поменять статус Эпика вручную

    @Test
    public abstract void changedStatusOfOneSubtaskShouldChangeEpicStatus(); // измененный статус подзадачи меняет статус Эпика

    @Test
    public abstract void shouldNotUpdateTaskWithWrongId (); // не должно обновлять задачу с неправильным id

    @Test
    public abstract void shouldNotUpdateSubtaskWithWrongId (); // не должно обновлять подзадачу с неправильным id


    @Test
    public abstract void shouldNotUpdateEpicWithWrongId (); // не должно обновлять эпик с неправильным id

    @Test
    public abstract void shouldGetNullTaskWithWrongId (); // должны получить null task с неправильным id

    @Test
    public abstract void shouldGetNullSubtaskWithWrongId (); // должны получить null subtask с неправильным id

    @Test
    public abstract void shouldGetNullEpicWithWrongId (); // должны получить null epic с неправильным id

    @Test
    public abstract void shouldGetSameTaskByCorrectId (); // должны получить одинаковые задачи по правильному id

    @Test
    public abstract void shouldGetSameSubtaskByCorrectId (); // должны получить одинаковые подзадачи по правильному id

    @Test
    public abstract void epicIdShouldBeSameBySubtaskInfo (); // по инфо подзадачи эпик id должен совпадать с эпиком в памяти

    @Test
    public abstract void shouldGetSameEpicByCorrectId (); // должны получить одинаковые эпики по правильному id

    @Test
    public abstract void shouldNotDeleteTaskWithWrongId (); // не должен удалять задачу с неправильным id

    @Test
    public abstract void shouldNotDeleteSubtaskWithWrongId (); // не должен удалять подзадачу с неправильным id

    @Test
    public abstract void shouldNotDeleteEpicWithWrongId (); // не должен удалять эпик с неправильным id

}