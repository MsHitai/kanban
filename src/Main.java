
public class Main {
    public static void main(String[] args) {

        Manager manager = new Manager();

        System.out.println("Создаем задачи...");
        manager.createTask();
        manager.createTask();
        manager.createSubTask();
        manager.createEpic();

        System.out.println("У нас создались следующие задачи: ");
        System.out.println(manager.tasks);
        System.out.println(manager.subTasks);
        System.out.println(manager.epics);

        System.out.println("Удаляем задачу под уникальным номером: 1. Остаётся в списке задач:");
        manager.deleteTaskByID(1);
        System.out.println(manager.tasks);
    }
}
