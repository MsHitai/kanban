import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.Status;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.Task;
import ru.yandex.practicum.service.InMemoryTaskManager;

public class Main {
    public static void main(String[] args) {

        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

        Task task1 = new Task("Покормить кота", "В 14-00", inMemoryTaskManager.createID(),
                Status.NEW);
        Task task2 = new Task("Погулять с хомяком", "Три раза", inMemoryTaskManager.createID(),
                Status.NEW);

        Epic epic1 = new Epic("Переехать", "До понедельника", inMemoryTaskManager.createID(),
                Status.NEW);
        SubTask subTask1 = new SubTask("Упаковать кота", "Не забыть его корм!", inMemoryTaskManager.createID(),
                Status.NEW, epic1.getUniqueID());
        SubTask subTask2 = new SubTask("Попрощаться со всеми", "Не забыть соседей!", inMemoryTaskManager.createID(),
                Status.NEW, epic1.getUniqueID());


        Epic epic2 = new Epic("Построить дом", "Из бревен", inMemoryTaskManager.createID(),
                Status.NEW);
        SubTask subTask3 = new SubTask("Заказать бревна", "Нужны черные!", inMemoryTaskManager.createID(),
                Status.NEW, epic2.getUniqueID());

        System.out.println("Создаем задачи...");
        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createTask(task2);
        inMemoryTaskManager.createEpic(epic1);
        inMemoryTaskManager.createSubTask(subTask1);
        inMemoryTaskManager.createSubTask(subTask2);
        inMemoryTaskManager.createEpic(epic2);
        inMemoryTaskManager.createSubTask(subTask3);

        System.out.println("У нас создались следующие задачи: ");
        System.out.println(inMemoryTaskManager.getTasks());
        System.out.println(inMemoryTaskManager.getSubtasks());
        System.out.println(inMemoryTaskManager.getEpics());

        System.out.println("Находим задачу под номером 1");
        System.out.println(inMemoryTaskManager.getTask(task1.getUniqueID()));

        System.out.println("Обновляем задачу под номером 1 на статус 'выполнено'. Проверяем...");
        Task task3 = new Task("Покормить кота", "В 14-00", task1.getUniqueID(),
                Status.DONE);
        inMemoryTaskManager.updateTask(task3);
        System.out.println(inMemoryTaskManager.getTask(task1.getUniqueID()));

        System.out.println("Удаляем задачу под номером: 1. Остаётся в списке задач:");
        inMemoryTaskManager.deleteTask(task1.getUniqueID());
        System.out.println(inMemoryTaskManager.getTasks());

        System.out.println("Удаляем список задач, проверяем...");
        inMemoryTaskManager.deleteAllTasks();
        System.out.println(inMemoryTaskManager.getTasks());

        System.out.println("Получаем список всех подзадач первого эпика...");
        System.out.println(inMemoryTaskManager.getSubTasksByEpics(epic1));

        System.out.println("Меняем статус подзадачи 'Упаковать кота' на 'в процессе'. Проверяем: ");
        SubTask subTask4 = new SubTask("Упаковать кота", "Не забыть его корм!", subTask1.getUniqueID(),
                Status.IN_PROGRESS, epic1.getUniqueID());
        inMemoryTaskManager.updateSubTask(subTask4);
        System.out.println(inMemoryTaskManager.getSubtask(subTask1.getUniqueID()));

        System.out.println("Проверяем статус первого эпика: ");
        System.out.println(inMemoryTaskManager.getEpic(epic1.getUniqueID()));

        System.out.println("Попробуем поменять статус первого эпика вручную на 'выполнен'. Проверяем:");
        Epic epic = new Epic("Переехать", "До понедельника", epic1.getUniqueID(),
                Status.DONE);
        inMemoryTaskManager.updateEpic(epic);
        System.out.println(inMemoryTaskManager.getEpic(epic.getUniqueID()));

        System.out.println("Удаляем подзадачу со статусом 'в процессе' у первого эпика и проверяем его статус: ");
        inMemoryTaskManager.deleteSubTask(subTask1.getUniqueID());
        System.out.println(inMemoryTaskManager.getEpic(epic1.getUniqueID()));
        System.out.println(inMemoryTaskManager.getSubtasks());

        System.out.println("Обновляем статус для подзадачи второго эпика на 'выполнен'. Проверяем: ");
        SubTask subTask5 = new SubTask("Заказать бревна", "Нужны черные!", subTask3.getUniqueID(),
                Status.DONE, epic2.getUniqueID());
        inMemoryTaskManager.updateSubTask(subTask5);
        System.out.println(inMemoryTaskManager.getSubTasksByEpics(epic2));

        System.out.println("Проверяем выполнен ли второй эпик...");
        System.out.println(inMemoryTaskManager.getEpic(epic2.getUniqueID()));

        System.out.println("Удаляем второй эпик. Проверяем его по ID, смотрим список его подзадач и " +
                "проверяем весь список подзадач: ");
        inMemoryTaskManager.deleteEpic(epic2.getUniqueID());
        System.out.println(inMemoryTaskManager.getEpic(epic2.getUniqueID()));
        System.out.println(inMemoryTaskManager.getSubTasksByEpics(epic2));
        System.out.println(inMemoryTaskManager.getSubtasks());

        System.out.println("Удаляем все эпики. Проверяем список эпиков и их подзадач: ");
        inMemoryTaskManager.deleteAllEpics();
        System.out.println(inMemoryTaskManager.getEpics());
        System.out.println(inMemoryTaskManager.getSubtasks());
    }
}
