package service;

import service.historyManager.HistoryManager;
import service.historyManager.InMemoryHistoryManager;
import service.taskManager.FileBackedTasksManager;
import service.taskManager.HttpTaskManager;
import service.taskManager.InMemoryTaskManager;
import service.taskManager.TaskManager;

import java.io.File;

public class Managers {

    public static TaskManager getDefaultTaskManager() {return new InMemoryTaskManager();
    }

    public static HistoryManager getHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getFileBacked(File file) {
        return new FileBackedTasksManager(file);
    }

    public static TaskManager getDefault(String uri) {
        return new HttpTaskManager(uri);
    }
}