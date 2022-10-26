package service;

import service.history.HistoryManager;
import service.history.InMemoryHistoryManager;
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