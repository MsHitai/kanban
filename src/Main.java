
public class Main {
    public static void main(String[] args) {

        Manager manager = new Manager();

        Task task = new Task("Новая задача", "Что нужно сделать", manager.createID(),
                "NEW", false);
        SubTask subTask = new SubTask("Новая подзадача", "Что нужно сделать", manager.createID(),
                "NEW", false);
        Epic epic = new Epic("Новый эпик", "Что нужно сделать", manager.createID(),
                "NEW", false);

        System.out.println("Создаем задачи...");
        manager.createTask(task, task.uniqueID);
        manager.createSubTask(subTask, subTask.uniqueID);
        manager.createEpic(epic, epic.uniqueID);

        System.out.println("У нас создались следующие задачи: ");
        System.out.println(manager.tasks);
        System.out.println(manager.subTasks);
        System.out.println(manager.epics);

        System.out.println("Удаляем задачу под уникальным номером: 1. Остаётся в списке задач:");
        manager.deleteTaskByID(manager.tasks, 1);
        System.out.println(manager.tasks);

        System.out.println("Удаляем подзадачу под уникальным номером: 3. Остаётся в списке задач:");
        manager.deleteTaskByID(manager.subTasks, 3);
        System.out.println(manager.subTasks);
    }
}
