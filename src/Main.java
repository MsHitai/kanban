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

        Epic epic2 = new Epic("Построить дом", "Из бревен", 0, Status.NEW);
        taskManager.createEpic(epic2);
        SubTask subTask3 = new SubTask("Заказать бревна", "Нужны черные!", 0, Status.NEW,
                epic2.getUniqueID());
        taskManager.createSubTask(subTask3);

        System.out.println("Создаем задачи...");

        System.out.println("У нас создались следующие задачи: ");
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getSubtasks());
        System.out.println(taskManager.getEpics());

        System.out.println("Находим задачу под номером 1");
        System.out.println(taskManager.getTask(task1.getUniqueID()));

        System.out.println("Обновляем задачу под номером 1 на статус 'выполнено'. Проверяем...");
        Task task3 = new Task("Покормить кота", "В 14-00", task1.getUniqueID(), Status.DONE);
        taskManager.updateTask(task3);
        System.out.println(taskManager.getTask(task1.getUniqueID()));

        System.out.println("Удаляем задачу под номером: 1. Остаётся в списке задач:");
        taskManager.deleteTask(task1.getUniqueID());
        System.out.println(taskManager.getTasks());

        System.out.println("Удаляем список задач, проверяем...");
        taskManager.deleteAllTasks();
        System.out.println(taskManager.getTasks());

        System.out.println();

        System.out.println("История запросов: ");
        taskManager.getHistory();

        System.out.println("Получаем список всех подзадач первого эпика...");
        System.out.println(taskManager.getSubTasksByEpics(epic1));

        System.out.println("Меняем статус подзадачи 'Упаковать кота' на 'в процессе'. Проверяем: ");
        SubTask subTask4 = new SubTask("Упаковать кота", "Не забыть его корм!", subTask1.getUniqueID(),
                Status.IN_PROGRESS, epic1.getUniqueID());
        taskManager.updateSubTask(subTask4);
        System.out.println(taskManager.getSubtask(subTask1.getUniqueID()));

        System.out.println("Проверяем статус первого эпика: ");
        System.out.println(taskManager.getEpic(epic1.getUniqueID()));

        System.out.println("Попробуем поменять статус первого эпика вручную на 'выполнен'. Проверяем:");
        Epic epic = new Epic("Переехать", "До понедельника", epic1.getUniqueID(), Status.DONE);
        taskManager.updateEpic(epic);
        System.out.println(taskManager.getEpic(epic.getUniqueID()));

        System.out.println("Удаляем подзадачу со статусом 'в процессе' у первого эпика и проверяем его статус: ");
        taskManager.deleteSubTask(subTask1.getUniqueID());
        System.out.println(taskManager.getEpic(epic1.getUniqueID()));
        System.out.println(taskManager.getSubtasks());

        System.out.println();

        System.out.println("История запросов: ");
        taskManager.getHistory();

        System.out.println("Обновляем статус для подзадачи второго эпика на 'выполнен'. Проверяем: ");
        SubTask subTask5 = new SubTask("Заказать бревна", "Нужны черные!", subTask3.getUniqueID(),
                Status.DONE, epic2.getUniqueID());
        taskManager.updateSubTask(subTask5);
        System.out.println(taskManager.getSubTasksByEpics(epic2));

        System.out.println("Проверяем выполнен ли второй эпик...");
        System.out.println(taskManager.getEpic(epic2.getUniqueID()));

        System.out.println("Удаляем второй эпик. Проверяем его по ID, смотрим список его подзадач и " +
                "проверяем весь список подзадач: ");
        taskManager.deleteEpic(epic2.getUniqueID());
        System.out.println(taskManager.getEpic(epic2.getUniqueID()));
        System.out.println(taskManager.getSubTasksByEpics(epic2));
        System.out.println(taskManager.getSubtasks());

        System.out.println("Удаляем все эпики. Проверяем список эпиков и их подзадач: ");
        taskManager.deleteAllEpics();
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());

        System.out.println("История запросов: ");
        taskManager.getHistory();
    }
}
