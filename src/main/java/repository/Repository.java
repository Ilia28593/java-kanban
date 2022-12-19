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
    private final TreeSet<Task> sortedTaskTree = new TreeSet<>(Comparator.comparing(Task::getStart));
    private final TreeSet<Task> waiteTimeTaskTree = new TreeSet<>(Comparator.comparing(Task::getType));
    private final TreeMap<LocalDateTime, Integer> checkFreeTime = new TreeMap<>(Comparator.comparing(LocalDateTime::getChronology));

    public void addSorted(Task task) {
        if(task.getStart()!=null) {
            if(waiteTimeTaskTree.contains(task)){
                waiteTimeTaskTree.remove(task);
            }
            sortedTaskTree.add(task);
        } else{
            waiteTimeTaskTree.add(task);
        }
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
        new Task(0);
    }

}
