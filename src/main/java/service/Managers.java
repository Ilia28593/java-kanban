package service;

import service.historyManager.HistoryManager;
import service.historyManager.InMemoryHistoryManager;
import service.taskManager.InMemoryTaskManager;
import service.taskManager.TaskManager;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getHistory() {
        return new InMemoryHistoryManager();
    }
}