package ru.yandex.practicum.service;

import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.enums.Status;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.Task;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

    protected Comparator<Task> taskComparator = (o1, o2) -> {
            if (o1.getStartTime() == null || o2.getStartTime() == null) {
                return 1;
            }
            return o1.getStartTime().compareTo(o2.getStartTime());
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
        if (checkTimeClashes(task)) {
            prioritizedTasks.add(task);
        }
    }
    @Override
    public void createSubTask(SubTask subTask) {
        subTask.setUniqueID(createID());
        subtasks.put(subTask.getUniqueID(), subTask);
        Epic epic = getEpic(subTask.getEpicId());
        epic.addSubtaskId(subTask.getUniqueID());
        updateEpicStatus(epic);
        updateEpicTime(epic);
        if (checkTimeClashes(subTask)) {
            prioritizedTasks.add(subTask);
        }
    }
    @Override
    public void createEpic(Epic epic) {
        epic.setUniqueID(createID());
        epics.put(epic.getUniqueID(), epic);
    }
    @Override
    public void updateTask(Task task) {
        int id = task.getUniqueID();
        if (tasks.containsKey(id)) {
            tasks.put(id, task);
            if (checkTimeClashes(task)) {
                prioritizedTasks.add(task);
            }
        }
    }
    @Override
    public Task getTask(int id) {
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
        if (tasks.containsKey(id)) {
            Task task = getTask(id);
            prioritizedTasks.remove(task);
            historyManager.remove(id);
            tasks.remove(id);
        }
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
            updateEpicTime(getEpic(epicId));
            if (checkTimeClashes(subTask)) {
                prioritizedTasks.add(subTask);
            }
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
            updateEpicTime(epic);
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
            updateEpicTime(epic);
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

    private boolean checkTimeClashes (Task task) { // проверяем пересечения времени
        if (task == null) {
            return true;
        }
        ArrayList<Task> prioritized = new ArrayList<>(prioritizedTasks);
        boolean noClashes = true;
        if (prioritized.size() == 0) {
            return true;
        }
        for (Task task1 : prioritized) {
            if (task.equals(task1) || task1.getStartTime() == null || task.getStartTime() == null) {
                continue;
            }
            /* пусть первая задача 14:02 // endTime 14:17
               а вторая 14:07          // endTime 14:22
               значит 14:07 is After 14:02 && Before 14:17*/
            if (task.getStartTime().isAfter(task1.getStartTime()) && task.getStartTime().isBefore(task1.getEndTime())) {
                noClashes = false;
            }
        }
        return noClashes;
    }

    private void updateEpicTime(Epic epic) {
        if (epic.getSubtaskIds() == null || epic.getSubtaskIds().isEmpty()) {
            epic.setStartTime(null);
            epic.setDuration(0);
            epic.setEndTime(null);
        } else {
            checkEpicStartTimeAndDuration(epic);
        }
    }

    private void checkEpicStartTimeAndDuration(Epic epic) {
        SubTask subTask = getSubtask(epic.getSubtaskIds().get(0)); // берем подзадачу по первому индексу эпика
        LocalDateTime minStartTime = subTask.getStartTime(); // задаем минимум для начального времени
        LocalDateTime maxEndTime = subTask.getEndTime(); // задаем минимум для конечного времени
        int duration;
        for (Integer subtaskId : epic.getSubtaskIds()) {
            subTask = getSubtask(subtaskId);
            if(subTask.getStartTime() == null) {
                continue;
            }
            if (subTask.getStartTime().isBefore(minStartTime)) {
                minStartTime = subTask.getStartTime();
            }

            if (subTask.getEndTime().isAfter(maxEndTime)) {
                maxEndTime = subTask.getEndTime();
            }
        }

        duration = (int) ChronoUnit.MINUTES.between(minStartTime, maxEndTime);

        epic.setStartTime(minStartTime);
        epic.setDuration(duration);
        epic.setEndTime(maxEndTime);
    }

    @Override
    public void updateEpic(Epic epic) {
        int epicID = epic.getUniqueID();
        if (epics.containsKey(epicID)) {
            epic.setSubtaskIds(getEpic(epic.getUniqueID()).getSubtaskIds());
            epics.put(epicID, epic);
            updateEpicStatus(getEpic(epicID));
            updateEpicTime(getEpic(epicID));
        }
    }
    @Override
    public void deleteEpic(int id) {
        if (epics.containsKey(id)) {
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
