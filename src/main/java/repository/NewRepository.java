package repository;

import model.DefaultTask;
import model.EpicTask;

import java.util.ArrayList;
import java.util.List;

public class NewRepository {
    private static List<DefaultTask> defaultTaskList = new ArrayList<>();
    private static List<EpicTask> epicTaskList = new ArrayList<>();

    public void setDefaultTaskList(DefaultTask defaultTaskList) {
        NewRepository.defaultTaskList.add(defaultTaskList);
    }

    public void setEpicTaskList(EpicTask epicTaskList) {
        NewRepository.epicTaskList.add(epicTaskList);
    }

    public static List<DefaultTask> getDefaultTaskList() {
        return defaultTaskList;
    }

    public static List<EpicTask> getEpicTaskList() {
        return epicTaskList;
    }
}
