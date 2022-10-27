package service;

import service.historyManager.HistoryManager;
import service.historyManager.InMemoryHistoryManager;
import service.taskManager.FileBackedTasksManager;
import service.taskManager.InMemoryTaskManager;
import service.taskManager.TaskManager;

import java.io.File;

public class Managers {

    public TaskManager getDefaultTaskManager() {return new InMemoryTaskManager();
    }

    public HistoryManager getHistory() {
        return new InMemoryHistoryManager();
    }

    public TaskManager getFileBacked(File file) {
        return new FileBackedTasksManager(file);
    }
}