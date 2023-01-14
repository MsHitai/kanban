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

    public int createID(){
        return ++uniqueID;
    }

    public void createTask(Task task, int uniqueID) {
        tasks.put(uniqueID, task);
    }

    public void createSubTask(SubTask subTask, int uniqueID) {
        subTasks.put(uniqueID, subTask);
    }

    public void createEpic(Epic epic, int uniqueID) {
        epics.put(uniqueID, epic);
    }

    public void deleteTaskByID(HashMap hashMap, int number) {
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

}
