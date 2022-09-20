package repository;

import model.EpicTask;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    private static final List<Task> TASK_LIST = new ArrayList<>();
    private static final List<EpicTask> epicTaskList = new ArrayList<>();
    private static final List<SubTask> SUB_TASK_LIST = new ArrayList<>();

    public List<SubTask> getSubtaskList() {
        return SUB_TASK_LIST;
    }

    public void addSubtaskList(SubTask subTaskList) {
        Repository.SUB_TASK_LIST.add(subTaskList);
    }

    public void addDefaultTaskList(Task taskList) {
        Repository.TASK_LIST.add(taskList);
    }

    public void addEpicTaskList(EpicTask epicTaskList) {
        Repository.epicTaskList.add(epicTaskList);
    }

    public List<Task> getDefaultTaskList() {
        return TASK_LIST;
    }

    public List<EpicTask> getEpicTaskList() {
        return epicTaskList;
    }
}
