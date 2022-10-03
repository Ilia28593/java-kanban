package service;

public class Managers {

    public TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public HistoryManager getHistory() {
        return new InMemoryHistoryManager();
    }
}