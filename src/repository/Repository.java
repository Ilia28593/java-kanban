package repository;

import entity.Task;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    private static final List<Task> tasks = new ArrayList<>();


    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        tasks.addAll(tasks);
    }

    public void setTask(Task task) {
        tasks.add(task);
    }

    public void clean() {
        Repository.tasks.clear();
    }

}
