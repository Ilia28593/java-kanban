package service;

import model.Task;

import java.util.ArrayList;

public interface HistoryManager {

    void remove(int id);

    void add(Task task);

    ArrayList<Task> getHistoryList();
}
