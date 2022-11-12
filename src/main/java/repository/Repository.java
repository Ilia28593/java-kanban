package repository;

import model.EpicTask;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Repository {
    private final Map<Integer, Task> taskMap = new HashMap<>();
    private final Map<Integer, EpicTask> epicTaskMap = new HashMap<>();
    private final Map<Integer, SubTask> subTaskMap = new HashMap<>();

    public Map<Integer, Task> getTaskMap() {
        return taskMap;
    }

    public Map<Integer, EpicTask> getEpicTaskMap() {
        return epicTaskMap;
    }

    public Map<Integer, SubTask> getSubTaskMap() {
        return subTaskMap;
    }

    public void addSubtaskMap(SubTask subTask) {
        subTaskMap.put(subTask.getId(), subTask);
    }

    public void addTaskMap(Task task) {
        taskMap.put(task.getId(), task);
    }

    public void addEpicTaskMap(EpicTask epicTask) {
        epicTaskMap.put(epicTask.getId(), epicTask);
    }

    public List<SubTask> getSubtaskList() {
        return new ArrayList<>(subTaskMap.values());
    }

    public List<Task> getTaskList() {
        return new ArrayList<>(taskMap.values());
    }

    public List<EpicTask> getEpicTaskList() {
        return new ArrayList<>(epicTaskMap.values());
    }
}
