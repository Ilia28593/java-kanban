package repository;

import model.DefaultTask;
import model.EpicTask;
import model.Subtask;

import java.util.ArrayList;
import java.util.List;

public class NewRepository {
    private static final List<DefaultTask> defaultTaskList = new ArrayList<>();
    private static final List<EpicTask> epicTaskList = new ArrayList<>();
    private static final List<Subtask> subtaskList = new ArrayList<>();

    public  List<Subtask> getSubtaskList() {
        return subtaskList;
    }

    public void setSubtaskList(Subtask subtaskList) {
        NewRepository.subtaskList.add(subtaskList);
    }

    public void setDefaultTaskList(DefaultTask defaultTaskList) {
        NewRepository.defaultTaskList.add(defaultTaskList);
    }

    public void setEpicTaskList(EpicTask epicTaskList) {
        NewRepository.epicTaskList.add(epicTaskList);
    }

    public  List<DefaultTask> getDefaultTaskList() {
        return defaultTaskList;
    }

    public List<EpicTask> getEpicTaskList() {
        return epicTaskList;
    }
}
