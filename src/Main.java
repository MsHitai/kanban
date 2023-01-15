
public class Main {
    public static void main(String[] args) {

        Manager manager = new Manager();

        Task task1 = new Task("Покормить кота", "В 14-00", manager.createID(),
                "NEW", false);
        Task task2 = new Task("Погулять с хомяком", "Три раза", manager.createID(),
                "NEW", false);

        Epic epic1 = new Epic("Переехать", "До понедельника", manager.createID(),
                "NEW", false);
        SubTask subTask1 = new SubTask("Упаковать кота", "Не забыть его корм!", manager.createID(),
                "NEW", false, epic1.uniqueID);
        SubTask subTask2 = new SubTask("Попрощаться со всеми", "Не забыть соседей!", manager.createID(),
                "NEW", false, epic1.uniqueID);


        Epic epic2 = new Epic("Построить дом", "Из бревен", manager.createID(),
                "NEW", false);
        SubTask subTask3 = new SubTask("Заказать бревна", "Нужны черные!", manager.createID(),
                "NEW", false, epic2.uniqueID);

        System.out.println("Создаем задачи...");
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createEpic(epic1);
        manager.createSubTask(subTask1);
        manager.createSubTask(subTask2);
        manager.sortSubTasksByEpics(epic1, epic1.uniqueID);
        manager.createEpic(epic2);
        manager.createSubTask(subTask3);
        manager.sortSubTasksByEpics(epic2, epic2.uniqueID);

        System.out.println("У нас создались следующие задачи: ");
        manager.listAllTasks();
        manager.listAllEpics();
        manager.listAllSubTasks();

        System.out.println("Находим задачу под номером 2");
        manager.getTaskByID(manager.tasks, 2);

        System.out.println("Обновляем задачу под номером 1 на статус 'в процессе'. Проверяем...");
        manager.refreshTask("Покормить кота", "В 14-00", task1.uniqueID, "IN_PROGRESS",
                false);
        manager.getTaskByID(manager.tasks, 1);

        System.out.println("Удаляем задачу под номером: 1. Остаётся в списке задач:");
        manager.deleteTaskByID(manager.tasks, 1);
        manager.listAllTasks();

        System.out.println("Удаляем список задач, проверяем...");
        manager.deleteAllTasks(manager.tasks);
        manager.listAllTasks();

        System.out.println("Получаем список всех подзадач первого эпика...");
        manager.getSubTasksByEpics(epic1);

        System.out.println("Проверяем статус первого эпика: ");
        System.out.println(epic1.checkStatus());

        System.out.println("Обновляем статус для подзадачи второго эпика на 'выполнен'. Проверяем: ");
        manager.refreshSubTask("Заказать бревна", "Нужны черные!", subTask3.uniqueID,
                "DONE", false, epic2.uniqueID);
        manager.getSubTasksByEpics(epic2);

        System.out.println("Проверяем выполнен ли второй эпик...");
        System.out.println(epic2.checkIsDone());

        System.out.println("Удаляем второй эпик. Проверяем его по ID и смотрим список его подзадач: ");
        manager.deleteEpicByID(manager.epics, epic2.uniqueID);
        manager.getTaskByID(manager.epics, epic2.uniqueID);
        manager.getSubTasksByEpics(epic2);


    }
}
