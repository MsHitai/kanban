import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.Status;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.Task;
import ru.yandex.practicum.service.Managers;
import ru.yandex.practicum.service.TaskManager;

public class Main {
    public static void main(String[] args) {

        final TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("Покормить кота", "В 14-00",0, Status.NEW);
        taskManager.createTask(task1);
        Task task2 = new Task("Погулять с хомяком", "Три раза", 0, Status.NEW);
        taskManager.createTask(task2);

        Epic epic1 = new Epic("Переехать", "До понедельника", 0, Status.NEW);
        taskManager.createEpic(epic1);
        SubTask subTask1 = new SubTask("Упаковать кота", "Не забыть его корм!", 0, Status.NEW,
                epic1.getUniqueID());
        taskManager.createSubTask(subTask1);
        SubTask subTask2 = new SubTask("Попрощаться со всеми", "Не забыть соседей!", 0,
                Status.NEW, epic1.getUniqueID());
        taskManager.createSubTask(subTask2);
        SubTask subTask3 = new SubTask("Нанять грузчиков", "Минимум четырех", 0, Status.NEW,
                epic1.getUniqueID());
        taskManager.createSubTask(subTask3);

        Epic epic2 = new Epic("Построить дом", "Из бревен", 0, Status.NEW);
        taskManager.createEpic(epic2);

        System.out.println("Создаем задачи...");

        System.out.println("У нас создались следующие задачи: ");
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getSubtasks());
        System.out.println(taskManager.getEpics());

        System.out.println();

        System.out.println("Запрашиваем все задачи по очереди");
        taskManager.getTask(task1.getUniqueID());
        taskManager.getTask(task2.getUniqueID());
        taskManager.getEpic(epic1.getUniqueID());
        taskManager.getSubtask(subTask1.getUniqueID());
        taskManager.getSubtask(subTask2.getUniqueID());
        taskManager.getSubtask(subTask3.getUniqueID());
        taskManager.getEpic(epic2.getUniqueID());

        System.out.println();

        System.out.println("История запросов: ");
        taskManager.getHistory();

        System.out.println();

        System.out.println("Запрашиваем все задачи хаотично");
        taskManager.getEpic(epic2.getUniqueID());
        taskManager.getSubtask(subTask3.getUniqueID());
        taskManager.getTask(task1.getUniqueID());
        taskManager.getSubtask(subTask1.getUniqueID());
        taskManager.getEpic(epic1.getUniqueID());
        taskManager.getSubtask(subTask2.getUniqueID());
        taskManager.getTask(task2.getUniqueID());

        System.out.println();

        System.out.println("История запросов: ");
        taskManager.getHistory();

        System.out.println();

        System.out.println("Удаляем все подзадачи, проверяем историю запросов:");
        taskManager.deleteAllSubTasks();
        taskManager.getHistory();

        System.out.println();

        System.out.println("Удаляем все задачи и эпики, проверяем историю запросов:");
        taskManager.deleteAllTasks();
        taskManager.deleteAllEpics();
        taskManager.getHistory();

        /*System.out.println("Удаляем задачу \"Покормить кота\". Проверяем осталась ли она в истории:");
        taskManager.deleteTask(task1.getUniqueID());
        taskManager.getHistory();

        System.out.println();

        System.out.println("Удаляем эпик \"Переехать\". Проверяем историю: ");
        taskManager.deleteEpic(epic1.getUniqueID());
        taskManager.getHistory();*/
    }
}
