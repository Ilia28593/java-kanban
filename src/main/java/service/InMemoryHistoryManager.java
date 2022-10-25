package service;

import model.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    CustomLinkedList customLinkedList = new CustomLinkedList();

    @Override
    public void remove(int id) {
        customLinkedList.remove(id);
    }

    @Override
    public void add(Task task) {
        remove(task.getId());
        customLinkedList.linkLast(task);
    }

    @Override
    public ArrayList<Task> getHistoryList() {
        return customLinkedList.getTasks();
    }
}


