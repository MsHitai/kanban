import java.util.HashMap;


public class Manager {
    int uniqueID;

    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, SubTask> subTasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();

    public int createID() {
        return ++uniqueID;
    }

    public void createTask(Task task) {
        tasks.put(task.uniqueID, task);
    }

    public void createSubTask(SubTask subTask) {
        subTasks.put(subTask.uniqueID, subTask);
    }

    public void createEpic(Epic epic) {
        epics.put(epic.uniqueID, epic);
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
        if (!epics.isEmpty()) {
            System.out.println(epics);
        } else {
            System.out.println("Список епиков пуст.");
        }
        if (!subTasks.isEmpty()) {
            System.out.println(subTasks);
        } else {
            System.out.println("Список подзадач пуст.");
        }
    }

}
