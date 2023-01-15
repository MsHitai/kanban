
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
        manager.createEpic(epic2);
        manager.createSubTask(subTask3);

        System.out.println("У нас создались следующие задачи: ");
        manager.listAllTasks();

        System.out.println("Удаляем задачу под уникальным номером: 1. Остаётся в списке задач:");
        manager.deleteTaskByID(manager.tasks, 1);
        manager.listAllTasks();
        //todo change the delete method of subtasks according to an epic
        System.out.println("Удаляем все подзадачи... Остаётся в списке задач:");
        manager.deleteTasks(manager.subTasks);
        manager.listAllTasks();
    }
}
