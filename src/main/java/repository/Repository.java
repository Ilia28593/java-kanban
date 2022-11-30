package repository;

import lombok.Data;
import model.EpicTask;
import model.SubTask;
import model.Task;

import java.time.LocalDateTime;
import java.util.*;

@Data
public class Repository {
    private final Map<Integer, Task> taskMap = new HashMap<>();
    private final Map<Integer, EpicTask> epicTaskMap = new HashMap<>();
    private final Map<Integer, SubTask> subTaskMap = new HashMap<>();
    private final Map<LocalDateTime, Integer> timeCompletedTask = new HashMap<>();
    private final TreeSet<Task> sortedTaskTree = new TreeSet<>(Comparator.comparing(Task::getStart));
    private final TreeMap<LocalDateTime, Integer> checkFreeTime = new TreeMap<>(Comparator.comparing(LocalDateTime::getChronology));

    public void addSorted(Task task) {
        sortedTaskTree.add(task);
    }

    public void addTimeCompletedTaskMap(LocalDateTime localDateTime, int id) {
        timeCompletedTask.put(localDateTime, id);
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

    public List<Task> listElement() {
        return new ArrayList<>() {{
            addAll(getTaskMap().values());
            addAll(getEpicTaskMap().values());
            addAll(getSubTaskMap().values());
        }};
    }

    public void cleanRepository() {
        epicTaskMap.clear();
        subTaskMap.clear();
        taskMap.clear();
        sortedTaskTree.clear();
        timeCompletedTask.clear();
    }

}
