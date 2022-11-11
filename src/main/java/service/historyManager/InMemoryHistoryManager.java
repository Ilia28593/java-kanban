package service.historyManager;

import model.Task;

import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final CustomLinkedList customLinkedList = new CustomLinkedList();

    @Override
    public void remove(int id) {
        customLinkedList.remove(id);
    }

    @Override
    public void add(Task task) {
        customLinkedList.linkLast(task);
    }

    @Override
    public List<Task> getHistoryList() {
        return customLinkedList.getTasks();
    }
}



