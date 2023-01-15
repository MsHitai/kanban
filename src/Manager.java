import java.util.ArrayList;
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

    public void refreshTask(String newName, String newDescription, int taskID, String status, boolean isDone) {
        Task task = new Task(newName, newDescription, taskID, status, isDone);
        tasks.put(taskID, task);
    }

    public void getTaskByID(HashMap<Integer, ?> hashMap, int number) { // выдает по номеру задачу/подзадачу/эпик
        if (hashMap != null) {
            if (hashMap.containsKey(number)) {
                System.out.println(hashMap.get(number));
            } else {
                System.out.println("Под этим номером ничего нет.");
            }
        } else {
            System.out.println("Список задач пуст.");
        }
    }

    public void deleteTaskByID(HashMap<Integer, Task> hashMap, int number) {
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

    public void deleteAllTasks(HashMap<Integer, Task> hashMap) {
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
    }

    public void refreshSubTask(String newName, String newDescription, int subTaskID, String status, boolean isDone,
                               int number) {
        SubTask subTask = new SubTask(newName, newDescription, subTaskID, status, isDone, number);
        if (subTasks.containsValue(subTask)) {
            deleteSubTaskByID(subTasks, subTaskID);
        }
        subTasks.put(subTaskID, subTask);
        if (epics.containsKey(number)) {
            sortSubTasksByEpics(getEpicByID(epics, number), number);

        }
    }

    public void deleteSubTasksByEpic(Epic epic) {
        if (epic.mySubTasks != null) {
            epic.mySubTasks.clear();
        } else {
            System.out.println("Нет подходящих подзадач для этого эпика.");
        }
    }

    public void deleteSubTaskByID(HashMap<Integer, SubTask> hashMap, int number) {
        if (!hashMap.isEmpty()) {
            for (SubTask subTask : hashMap.values()) {
                if (subTask.uniqueID == number) {
                    hashMap.remove(subTask.uniqueID);
                }
            }
        } else {
            System.out.println("Список подзадач пуст.");
        }
    }

    public void listAllSubTasks() {
        if (!subTasks.isEmpty()) {
            System.out.println(subTasks);
        } else {
            System.out.println("Список подзадач пуст.");
        }
    }

    public void listAllEpics() {
        if (!epics.isEmpty()) {
            System.out.println(epics);
        } else {
            System.out.println("Список епиков пуст.");
        }
    }

    private Epic getEpicByID(HashMap<Integer, Epic> hashMap, int number) {
        Epic epic = null;
        if (hashMap != null) {
            if (hashMap.containsKey(number)) {
                epic = hashMap.get(number);
            } else {
                System.out.println("Нет эпика под таким номером.");
            }
        } else {
            System.out.println("Список эпиков пуст.");
        }
        return epic;
    }

    public void deleteEpicByID(HashMap<Integer, Epic> hashMap, int number) {
        if (hashMap != null) {
            if (hashMap.containsKey(number)) {
                deleteSubTasksByEpic(hashMap.get(number));
                hashMap.remove(number);
            } else {
                System.out.println("Нет эпика под таким номером.");
            }
        } else {
            System.out.println("Список эпиков пуст.");
        }
    }

    public void sortSubTasksByEpics(Epic epic, int epicID) {
        ArrayList<SubTask> subtasks = new ArrayList<>();
        for (SubTask subTask : subTasks.values()) {
            if (subTask.myEpicID == epicID) {
                subtasks.add(subTask);
            }
        }
        if (subtasks.isEmpty()) {
            System.out.println("Нет подходящих подзадач для этого эпика.");
        } else {
            epic.fillMySubTasks(subtasks);
        }
    }

    public void getSubTasksByEpics(Epic epic) {
        if (epic.mySubTasks != null) {
            if (epic.mySubTasks.isEmpty()) {
                System.out.println("Список подзадач для этого эпика пуст.");
                return;
            }
            System.out.println(epic.mySubTasks);
        } else {
            System.out.println("Нет подходящих подзадач для этого эпика.");
        }
    }

}
