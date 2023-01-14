
public class Main {
    public static void main(String[] args) {

        Manager manager = new Manager();

        System.out.println("Создаем задачи...");
        manager.createTask("Переехать", "До понедельника", manager.createID(),
                "NEW", false);
        manager.createTask("Построить дом", "Из бревен", manager.createID(),
                "NEW", false);
        manager.createSubTask("Упаковать кота", "Не забыть его корм!", manager.createID(),
                "NEW", false);
        manager.createSubTask("Заказать бревна", "Нужны черные!", manager.createID(),
                "NEW", false);
        manager.createEpic("Новый эпик", "Что нужно сделать", manager.createID(),
                "NEW", false);

        System.out.println("У нас создались следующие задачи: ");
        manager.listAllTasks();

        System.out.println("Удаляем задачу под уникальным номером: 1. Остаётся в списке задач:");
        manager.deleteTaskByID(manager.tasks, 1);
        manager.listAllTasks();

        System.out.println("Удаляем все подзадачи... Остаётся в списке задач:");
        manager.deleteTasks(manager.subTasks);
        manager.listAllTasks();
    }
}
