package repository;

import model.EpicTask;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    private static final List<Task> taskList = new ArrayList<>();
    private static final List<EpicTask> epicTaskList = new ArrayList<>();
    private static final List<SubTask> subTaskList = new ArrayList<>();

    public List<SubTask> getSubtaskList() {
        return subTaskList;
    }

    public void addSubtaskList(SubTask subTaskList) {
        Repository.subTaskList.add(subTaskList);
    }

    public void addTaskList(Task taskList) {
        Repository.taskList.add(taskList);
    }

    public void addEpicTaskList(EpicTask epicTaskList) {
        Repository.epicTaskList.add(epicTaskList);
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public List<EpicTask> getEpicTaskList() {
        return epicTaskList;
    }
}
