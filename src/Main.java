import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.Status;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.Task;
import ru.yandex.practicum.service.Managers;

public class Main {
    public static void main(String[] args) {

        Task task1 = new Task("Покормить кота", "В 14-00", Managers.getDefault().createID(),
                Status.NEW);
        Task task2 = new Task("Погулять с хомяком", "Три раза", Managers.getDefault().createID(),
                Status.NEW);

        Epic epic1 = new Epic("Переехать", "До понедельника", Managers.getDefault().createID(),
                Status.NEW);
        SubTask subTask1 = new SubTask("Упаковать кота", "Не забыть его корм!", Managers.getDefault().createID(),
                Status.NEW, epic1.getUniqueID());
        SubTask subTask2 = new SubTask("Попрощаться со всеми", "Не забыть соседей!", Managers.getDefault().createID(),
                Status.NEW, epic1.getUniqueID());


        Epic epic2 = new Epic("Построить дом", "Из бревен", Managers.getDefault().createID(),
                Status.NEW);
        SubTask subTask3 = new SubTask("Заказать бревна", "Нужны черные!", Managers.getDefault().createID(),
                Status.NEW, epic2.getUniqueID());

        System.out.println("Создаем задачи...");
        Managers.getDefault().createTask(task1);
        Managers.getDefault().createTask(task2);
        Managers.getDefault().createEpic(epic1);
        Managers.getDefault().createSubTask(subTask1);
        Managers.getDefault().createSubTask(subTask2);
        Managers.getDefault().createEpic(epic2);
        Managers.getDefault().createSubTask(subTask3);

        System.out.println("У нас создались следующие задачи: ");
        System.out.println(Managers.getDefault().getTasks());
        System.out.println(Managers.getDefault().getSubtasks());
        System.out.println(Managers.getDefault().getEpics());

        System.out.println("Находим задачу под номером 1");
        System.out.println(Managers.getDefault().getTask(task1.getUniqueID()));

        System.out.println("Обновляем задачу под номером 1 на статус 'выполнено'. Проверяем...");
        Task task3 = new Task("Покормить кота", "В 14-00", task1.getUniqueID(),
                Status.DONE);
        Managers.getDefault().updateTask(task3);
        System.out.println(Managers.getDefault().getTask(task1.getUniqueID()));

        System.out.println("Удаляем задачу под номером: 1. Остаётся в списке задач:");
        Managers.getDefault().deleteTask(task1.getUniqueID());
        System.out.println(Managers.getDefault().getTasks());

        System.out.println("Удаляем список задач, проверяем...");
        Managers.getDefault().deleteAllTasks();
        System.out.println(Managers.getDefault().getTasks());

        System.out.println(); // добавила строчку, чтобы выделить историю запросов

        System.out.println("История запросов: ");
        System.out.println(Managers.getDefaultHistory().getHistory());

        System.out.println("Получаем список всех подзадач первого эпика...");
        System.out.println(Managers.getDefault().getSubTasksByEpics(epic1));

        System.out.println("Меняем статус подзадачи 'Упаковать кота' на 'в процессе'. Проверяем: ");
        SubTask subTask4 = new SubTask("Упаковать кота", "Не забыть его корм!", subTask1.getUniqueID(),
                Status.IN_PROGRESS, epic1.getUniqueID());
        Managers.getDefault().updateSubTask(subTask4);
        System.out.println(Managers.getDefault().getSubtask(subTask1.getUniqueID()));

        System.out.println("Проверяем статус первого эпика: ");
        System.out.println(Managers.getDefault().getEpic(epic1.getUniqueID()));

        System.out.println("Попробуем поменять статус первого эпика вручную на 'выполнен'. Проверяем:");
        Epic epic = new Epic("Переехать", "До понедельника", epic1.getUniqueID(),
                Status.DONE);
        Managers.getDefault().updateEpic(epic);
        System.out.println(Managers.getDefault().getEpic(epic.getUniqueID()));

        System.out.println("Удаляем подзадачу со статусом 'в процессе' у первого эпика и проверяем его статус: ");
        Managers.getDefault().deleteSubTask(subTask1.getUniqueID());
        System.out.println(Managers.getDefault().getEpic(epic1.getUniqueID()));
        System.out.println(Managers.getDefault().getSubtasks());

        System.out.println(); // добавила строчку, чтобы выделить историю запросов

        System.out.println("История запросов: ");
        System.out.println(Managers.getDefaultHistory().getHistory());

        System.out.println("Обновляем статус для подзадачи второго эпика на 'выполнен'. Проверяем: ");
        SubTask subTask5 = new SubTask("Заказать бревна", "Нужны черные!", subTask3.getUniqueID(),
                Status.DONE, epic2.getUniqueID());
        Managers.getDefault().updateSubTask(subTask5);
        System.out.println(Managers.getDefault().getSubTasksByEpics(epic2));

        System.out.println("Проверяем выполнен ли второй эпик...");
        System.out.println(Managers.getDefault().getEpic(epic2.getUniqueID()));

        System.out.println("Удаляем второй эпик. Проверяем его по ID, смотрим список его подзадач и " +
                "проверяем весь список подзадач: ");
        Managers.getDefault().deleteEpic(epic2.getUniqueID());
        System.out.println(Managers.getDefault().getEpic(epic2.getUniqueID()));
        System.out.println(Managers.getDefault().getSubTasksByEpics(epic2));
        System.out.println(Managers.getDefault().getSubtasks());

        System.out.println("Удаляем все эпики. Проверяем список эпиков и их подзадач: ");
        Managers.getDefault().deleteAllEpics();
        System.out.println(Managers.getDefault().getEpics());
        System.out.println(Managers.getDefault().getSubtasks());

        System.out.println("История запросов: ");
        System.out.println(Managers.getDefaultHistory().getHistory());
    }
}
