package repository;

import model.EpicTask;
import model.SubTask;
import model.Task;

import java.time.LocalDateTime;
import java.util.*;

public class Repository {
    private final Map<Integer, Task> taskMap = new HashMap<>();
    private final Map<Integer, EpicTask> epicTaskMap = new HashMap<>();
    private final Map<Integer, SubTask> subTaskMap = new HashMap<>();
    private final Map<LocalDateTime, Integer> timeCompletedTask = new HashMap<>();
    private final TreeSet<Task> sortedTaskTree = new TreeSet<>(Comparator.comparing(Task::getStart));

    public TreeSet<Task> getSortedTaskTree() {
        return sortedTaskTree;
    }

    public void addSorted(Task task) {
        sortedTaskTree.add(task);
    }

    public Map<Integer, Task> getTaskMap() {
        return taskMap;
    }

    public Map<Integer, EpicTask> getEpicTaskMap() {
        return epicTaskMap;
    }

    public Map<LocalDateTime, Integer> getTimeCompletedTaskMap() {
        return timeCompletedTask;
    }

    public void addTimeCompletedTaskMap(LocalDateTime localDateTime, int id) {
        timeCompletedTask.put(localDateTime, id);
    }

    public Map<Integer, SubTask> getSubTaskMap() {
        return subTaskMap;
    }

    public void addSubtaskMap(SubTask subTask) {
        subTaskMap.put(subTask.getId(), subTask);
        addSorted(subTask);
    }

    public void addTaskMap(Task task) {
        taskMap.put(task.getId(), task);
        addSorted(task);
    }

    public void addEpicTaskMap(EpicTask epicTask) {
        epicTaskMap.put(epicTask.getId(), epicTask);
        addSorted(epicTask);
    }

    public List<SubTask> getSubTaskList() {
        return new ArrayList<>(subTaskMap.values());
    }

    public List<Task> getTaskList() {
        return new ArrayList<>(taskMap.values());
    }

    public List<EpicTask> getEpicTaskList() {
        return new ArrayList<>(epicTaskMap.values());
    }

}
