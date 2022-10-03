package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryHistoryManager implements HistoryManager {

    private static final List<Task> tasks = new ArrayList<>();

    public void add(Task task) {
        Optional<Task> first = tasks.stream().filter(t -> t.getId() == task.getId()).findFirst();
        if (first.isEmpty()) {
            if (tasks.size() <= 10) {
                tasks.add(task);
            } else {
                tasks.remove(0);
                tasks.add(task);
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return tasks;
    }
}
