import java.util.HashMap;


public class Manager {

    String name;
    String description;
    int uniqueID;
    String status;
    boolean isDone;

    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, SubTask> subTasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();

    public void createTask() {
        name = "Новая задача";
        description = "Что нужно сделать";
        uniqueID++;
        Task task = new Task(name, description, uniqueID, "NEW", false);
        tasks.put(uniqueID, task);
    }

    public void createSubTask() {
        name = "Новая подзадача";
        description = "Что нужно сделать";
        uniqueID++;
        SubTask subTask = new SubTask(name, description, uniqueID, "NEW", false);
        subTasks.put(uniqueID, subTask);
    }

    public void createEpic() {
        name = "Новый эпик";
        description = "Что нужно сделать";
        uniqueID++;
        Epic epic = new Epic(name, description, uniqueID, "NEW", false);
        epics.put(uniqueID, epic);
    }

    public void deleteTaskByID(int number) {
        if (!tasks.isEmpty()) {
            if (tasks.containsKey(number)) {
                tasks.remove(number);
            } else {
                System.out.println("Нет задачи под таким номером.");
            }
        } else {
            System.out.println("Список задач пуст.");
        }
    }

}
