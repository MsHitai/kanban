import java.util.HashMap;

public class Manager {

    String name;
    String description;
    int uniqueID;
    String status;
    boolean isDone;

    Task task = new Task(name, description, uniqueID, status, isDone);

    HashMap<Integer, Task> tasks = new HashMap<>();

    SubTask subTask = new SubTask(name, description, uniqueID, status, isDone);

    HashMap<Integer, SubTask> subTasks = new HashMap<>();

    Epic epic = new Epic(name, description, uniqueID, status, isDone);

    HashMap<Integer, Epic> epics = new HashMap<>();


}
