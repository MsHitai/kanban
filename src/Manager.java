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

    public int createID() {
        return ++uniqueID;
    }

    public void createTask(String newName, String newDescription, int newID, String newStatus, boolean isDone) {
        Task task = new Task(newName, newDescription, newID, newStatus, isDone);
        tasks.put(newID, task);
    }

    public void createSubTask(String newName, String newDescription, int newID, String newStatus, boolean isDone) {
        SubTask subTask = new SubTask(newName, newDescription, newID, newStatus, isDone);
        subTasks.put(newID, subTask);
    }

    public void createEpic(String newName, String newDescription, int newID, String newStatus, boolean isDone) {
        Epic epic = new Epic(newName, newDescription, newID, newStatus, isDone);
        epics.put(newID, epic);
    }

    public void deleteTaskByID(HashMap<Integer, ?> hashMap, int number) {
        if (!hashMap.isEmpty()) {
            if (hashMap.containsKey(number)) {
                hashMap.remove(number);
            } else {
                System.out.println("Нет задачи под таким номером.");
            }
        } else {
            System.out.println("Список задач пуст.");
        }
    }

    public void deleteTasks(HashMap<Integer, ?> hashMap) {
        if (!hashMap.isEmpty()) {
            hashMap.clear();
        } else {
            System.out.println("Список задач пуст.");
        }
    }

    public void listAllTasks() {
        if (!tasks.isEmpty()) {
            System.out.println(tasks);
        } else {
            System.out.println("Список задач пуст.");
        }
        if (!subTasks.isEmpty()) {
            System.out.println(subTasks);
        } else {
            System.out.println("Список подзадач пуст.");
        }
        if (!epics.isEmpty()) {
            System.out.println(epics);
        } else {
            System.out.println("Список епиков пуст.");
        }
    }

}
