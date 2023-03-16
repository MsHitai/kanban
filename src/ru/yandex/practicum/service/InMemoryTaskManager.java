package ru.yandex.practicum.service;

import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.enums.Status;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.Task;

import java.time.LocalDateTime;
import java.util.*;


public class InMemoryTaskManager implements TaskManager{
    private int uniqueId = 0;
    private final HistoryManager<Task> historyManager = Managers.getDefaultHistory();

    protected HistoryManager<Task> getHistoryManager() {
        return historyManager;
    }

    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, SubTask> subtasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();

    protected Comparator<Task> taskComparator = new Comparator<Task>() {
        @Override
        public int compare(Task o1, Task o2) { // todo make some kind of null sorting thing...
            if (o1.getStartTime() == null || o2.getStartTime() == null) {
                return 0;
            }
            return o1.getStartTime().compareTo(o2.getStartTime());
        }
    };

    protected final Set<Task> prioritizedTasks = new TreeSet<>(taskComparator);

    private int createID() {
        return ++uniqueId;
    }
    @Override
    public List<Task> getTasks() {

        return new ArrayList<>(this.tasks.values());
    }
    @Override
    public List<SubTask> getSubtasks() {

        return new ArrayList<>(this.subtasks.values());
    }
    @Override
    public List<Epic> getEpics() {

        return new ArrayList<>(this.epics.values());
    }

    @Override
    public void createTask(Task task) {
        task.setUniqueID(createID());
        tasks.put(task.getUniqueID(), task);
        prioritizedTasks.add(task);
    }
    @Override
    public void createSubTask(SubTask subTask) {
        subTask.setUniqueID(createID());
        subtasks.put(subTask.getUniqueID(), subTask);
        Epic epic = getEpic(subTask.getEpicId());
        epic.addSubtaskId(subTask.getUniqueID());
        updateEpicStatus(epic);
        prioritizedTasks.add(subTask);
    }
    @Override
    public void createEpic(Epic epic) {
        epic.setUniqueID(createID());
        epics.put(epic.getUniqueID(), epic);
        prioritizedTasks.add(epic);
    }
    @Override
    public void updateTask(Task task) {
        int id = task.getUniqueID();
        if (tasks.containsKey(id)) {
            tasks.put(id, task);
            prioritizedTasks.add(task);
        }
    }
    @Override
    public Task getTask(int id) { // todo добавить сюда проверку, либо сделать исключение, либо getOrDefault
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }
    @Override
    public SubTask getSubtask(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }
    @Override
    public Epic getEpic(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }
    @Override
    public void deleteTask(int id) {
        Task task = getTask(id);
        prioritizedTasks.remove(task);
        historyManager.remove(id);
        tasks.remove(id);
    }
    @Override
    public void deleteAllTasks() {
        for (Integer id : tasks.keySet()) {
            Task task = getTask(id);
            prioritizedTasks.remove(task);
            historyManager.remove(id);
        }
        tasks.clear();
    }
    @Override
    public void updateSubTask(SubTask subTask) {
        int id = subTask.getUniqueID();
        int epicId = subTask.getEpicId();
        if (subtasks.containsKey(id) && epics.containsKey(epicId)) {
            subtasks.put(id, subTask);
            updateEpicStatus(getEpic(epicId));
        }
    }
    @Override
    public void deleteAllSubTasks() {
        for (Integer id : subtasks.keySet()) {
            SubTask subTask = getSubtask(id);
            prioritizedTasks.remove(subTask);
            historyManager.remove(id);
        }
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtaskIds().clear();
            updateEpicStatus(epic);
        }

    }
    @Override
    public void deleteSubTask(int id) {
        if (subtasks.containsKey(id)) {
            historyManager.remove(id);
            SubTask subTask = subtasks.remove(id);
            if (subTask == null) {
                return;
            }
            prioritizedTasks.remove(subTask);
            Epic epic = epics.get(subTask.getEpicId());
            epic.removeSubtaskId(id);
            updateEpicStatus(epic);
        }
    }

    private void updateEpicStatus(Epic epic) {
        if ((epic.getSubtaskIds() == null) || epic.getSubtaskIds().isEmpty()) {
            epic.setStatus(Status.NEW);
        } else {
            checkSubtaskStatus(epic);
        }
    }

    private void checkSubtaskStatus(Epic epic) {
        if (!getSubTasksByEpics(epic).isEmpty()) {
            int newSum = getSubTasksByEpics(epic).size();
            int doneSum = getSubTasksByEpics(epic).size();
            for (SubTask subTask : getSubTasksByEpics(epic)) {
                if (subTask == null) {
                    continue;
                }
                if (subTask.getStatus().equals(Status.NEW)) {
                    newSum--;
                } else if (subTask.getStatus().equals(Status.DONE)) {
                    doneSum--;
                }
            }
            if (newSum == 0) {
                epic.setStatus(Status.NEW);
            } else if (doneSum == 0) {
                epic.setStatus(Status.DONE);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
    }
    @Override
    public void updateEpic(Epic epic) {
        int epicID = epic.getUniqueID();
        if (epics.containsKey(epicID)) {
            epic.setSubtaskIds(getEpic(epic.getUniqueID()).getSubtaskIds());
            epics.put(epicID, epic);
            updateEpicStatus(getEpic(epicID));
        }
    }
    @Override
    public void deleteEpic(int id) {
        if (epics.containsKey(id)) {
            Epic epic = getEpic(id);
            prioritizedTasks.remove(epic);
            ArrayList<Integer> tasksId = getEpic(id).getSubtaskIds();
            if (tasksId != null) {
                for (Integer subtaskId : tasksId) {
                    historyManager.remove(subtaskId);
                    subtasks.remove(subtaskId);
                }
                epics.remove(id);
            }
            historyManager.remove(id);
        }
    }
    @Override
    public void deleteAllEpics() {
        for (Integer id : epics.keySet()) {
            Epic epic = getEpic(id);
            prioritizedTasks.remove(epic);
            historyManager.remove(id);
        }
        epics.clear();
        for (Integer id : subtasks.keySet()) {
            SubTask subTask = getSubtask(id);
            prioritizedTasks.remove(subTask);
            historyManager.remove(id);
        }
        subtasks.clear();
    }
    @Override
    public ArrayList<SubTask> getSubTasksByEpics(Epic epic) {
        ArrayList<SubTask> tasks = new ArrayList<>();
        for (int id : epic.getSubtaskIds()) {
            tasks.add(subtasks.get(id));
        }
        return tasks;
    }

    @Override
    public void getHistory(){
        System.out.println(historyManager.getHistory());
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }
}
