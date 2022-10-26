package repository;

import model.EpicTask;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    private final List<Task> taskList = new ArrayList<>();
    private final List<EpicTask> epicTaskList = new ArrayList<>();
    private final List<SubTask> subTaskList = new ArrayList<>();

    public List<SubTask> getSubtaskList() {
        return subTaskList;
    }

    public void addSubtaskList(SubTask subTask) {
        subTaskList.add(subTask);
    }

    public void addTaskList(Task task) {
        taskList.add(task);
    }

    public void addEpicTaskList(EpicTask epicTask) {
       epicTaskList.add(epicTask);
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public List<EpicTask> getEpicTaskList() {
        return epicTaskList;
    }
}
