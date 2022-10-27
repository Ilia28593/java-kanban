package service;

import service.historyManager.HistoryManager;
import service.historyManager.InMemoryHistoryManager;
import service.taskManager.InMemoryTaskManager;
import service.taskManager.TaskManager;

public class Managers {

    public TaskManager getDefaultTaskManager() {
        return new InMemoryTaskManager(getHistory());
    }

    public HistoryManager getHistory() {
        return new InMemoryHistoryManager();
    }
}