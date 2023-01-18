import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.Task;
import ru.yandex.practicum.service.Manager;

public class Main {
    public static void main(String[] args) {

        Manager manager = new Manager();

        Task task1 = new Task("Покормить кота", "В 14-00", manager.createID(),
                "NEW");
        Task task2 = new Task("Погулять с хомяком", "Три раза", manager.createID(),
                "NEW");

        Epic epic1 = new Epic("Переехать", "До понедельника", manager.createID(),
                "NEW");
        SubTask subTask1 = new SubTask("Упаковать кота", "Не забыть его корм!", manager.createID(),
                "NEW", epic1.getUniqueID());
        SubTask subTask2 = new SubTask("Попрощаться со всеми", "Не забыть соседей!", manager.createID(),
                "NEW", epic1.getUniqueID());


        Epic epic2 = new Epic("Построить дом", "Из бревен", manager.createID(),
                "NEW");
        SubTask subTask3 = new SubTask("Заказать бревна", "Нужны черные!", manager.createID(),
                "NEW", epic2.getUniqueID());

        System.out.println("Создаем задачи...");
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createEpic(epic1);
        manager.createSubTask(subTask1);
        manager.createSubTask(subTask2);
        manager.sortSubTasksByEpics(epic1, epic1.getUniqueID());
        manager.createEpic(epic2);
        manager.createSubTask(subTask3);
        manager.sortSubTasksByEpics(epic2, epic2.getUniqueID());

        System.out.println("У нас создались следующие задачи: ");
        System.out.println(manager.getTasks());
        System.out.println(manager.getSubtasks());
        System.out.println(manager.getEpics());

        System.out.println("Находим задачу под номером 1");
        System.out.println(manager.getTask(task1.getUniqueID()));

        System.out.println("Обновляем задачу под номером 1 на статус 'выполнено'. Проверяем...");
        Task task3 = new Task("Покормить кота", "В 14-00", task1.getUniqueID(),
                "DONE");
        manager.updateTask(task3);
        System.out.println(manager.getTask(task1.getUniqueID()));

        System.out.println("Удаляем задачу под номером: 1. Остаётся в списке задач:");
        manager.deleteTask(task1.getUniqueID());
        System.out.println(manager.getTasks());

        System.out.println("Удаляем список задач, проверяем...");
        manager.deleteAllTasks();
        System.out.println(manager.getTasks());

        System.out.println("Получаем список всех подзадач первого эпика...");
        System.out.println(manager.getSubTasksByEpics(epic1));

        System.out.println("Меняем статус подзадачи 'Упаковать кота' на 'в процессе'. Проверяем: ");
        SubTask subTask4 = new SubTask("Упаковать кота", "Не забыть его корм!", subTask1.getUniqueID(),
                "IN_PROGRESS", epic1.getUniqueID());
        manager.updateSubTask(subTask4);
        System.out.println(manager.getSubtask(subTask1.getUniqueID()));

        System.out.println("Проверяем статус первого эпика: ");
        System.out.println(manager.getEpic(epic1.getUniqueID()));

        System.out.println("Попробуем поменять статус первого эпика вручную на 'выполнен'. Проверяем:");
        Epic epic = new Epic("Переехать", "До понедельника", epic1.getUniqueID(),
                "DONE");
        manager.updateEpic(epic);
        System.out.println(manager.getEpic(epic.getUniqueID()));

        System.out.println("Удаляем подзадачу со статусом 'в процессе' у первого эпика и проверяем его статус: ");
        manager.deleteSubTask(subTask1.getUniqueID());
        System.out.println(manager.getEpic(epic1.getUniqueID()));
        System.out.println(manager.getSubtasks());

        System.out.println("Обновляем статус для подзадачи второго эпика на 'выполнен'. Проверяем: ");
        SubTask subTask5 = new SubTask("Заказать бревна", "Нужны черные!", subTask3.getUniqueID(),
                "DONE", epic2.getUniqueID());
        manager.updateSubTask(subTask5);
        System.out.println(manager.getSubTasksByEpics(epic2));

        System.out.println("Проверяем выполнен ли второй эпик...");
        System.out.println(manager.getEpic(epic2.getUniqueID()));

        System.out.println("Удаляем второй эпик. Проверяем его по ID, смотрим список его подзадач и " +
                "проверяем весь список подзадач: ");
        manager.deleteEpic(epic2.getUniqueID());
        System.out.println(manager.getEpic(epic2.getUniqueID()));
        System.out.println(manager.getSubTasksByEpics(epic2));
        System.out.println(manager.getSubtasks());

        System.out.println("Удаляем все эпики. Проверяем список эпиков и их подзадач: ");
        manager.deleteAllEpics();
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());
    }
}
